/**
 * This file is part of Sponge Config, licensed under the MIT License (Apache2).
 *
 * Copyright (C) 2011-2012 Typesafe Inc. <http://typesafe.com>
 * Adaptations Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.spongepowered.config.impl

import org.junit.Assert._
import org.junit._
import net.liftweb.{ json => lift }
import java.io.Reader
import java.io.StringReader
import org.spongepowered.config._
import java.util.HashMap
import java.io.File
import org.junit.runner.RunWith
import org.junit.runners.AllTests

class EquivalentsTest extends TestUtils {

    private def equivDirs() = {
        val rawEquivs = resourceDir.listFiles()
        val equivs = rawEquivs.filter({ f => f.getName().startsWith("equiv") })
        equivs
    }

    private def filesForEquiv(equiv: File) = {
        val rawFiles = equiv.listFiles()
        val files = rawFiles.filter({ f => f.getName().endsWith(".json") || f.getName().endsWith(".conf") })
        files
    }

    private def postParse(value: ConfigValue) = {
        value match {
            case v: AbstractConfigObject =>
                // for purposes of these tests, substitutions are only
                // against the same file's root, and without looking at
                // system prop or env variable fallbacks.
                ResolveContext.resolve(v, v, ConfigResolveOptions.noSystem())
            case v =>
                v
        }
    }

    private def parse(flavor: ConfigSyntax, f: File) = {
        val options = ConfigParseOptions.defaults().setSyntax(flavor)
        postParse(ConfigFactory.parseFile(f, options).root)
    }

    private def parse(f: File) = {
        val options = ConfigParseOptions.defaults()
        postParse(ConfigFactory.parseFile(f, options).root)
    }

    // would like each "equivNN" directory to be a suite and each file in the dir
    // to be a test, but not sure how to convince junit to do that.
    @Test
    def testEquivalents() {
        var dirCount = 0
        var fileCount = 0
        for (equiv <- equivDirs()) {
            dirCount += 1

            val files = filesForEquiv(equiv)
            val (originals, others) = files.partition({ f => f.getName().startsWith("original.") })
            if (originals.isEmpty)
                throw new RuntimeException("Need a file named 'original' in " + equiv.getPath())
            if (originals.size > 1)
                throw new RuntimeException("Multiple 'original' files in " + equiv.getPath() + ": " + originals)
            val original = parse(originals(0))

            for (testFile <- others) {
                fileCount += 1
                val value = parse(testFile)
                describeFailure(testFile.getPath()) {
                    try {
                        assertEquals(original, value)
                    } catch {
                        case e: Throwable =>
                            showDiff(original, value)
                            throw e
                    }
                }

                // check that all .json files can be parsed as .conf,
                // i.e. .conf must be a superset of JSON
                if (testFile.getName().endsWith(".json")) {
                    val parsedAsConf = parse(ConfigSyntax.CONF, testFile)
                    describeFailure(testFile.getPath() + " parsed as .conf") {
                        try {
                            assertEquals(original, parsedAsConf)
                        } catch {
                            case e: Throwable =>
                                showDiff(original, parsedAsConf)
                                throw e
                        }
                    }
                }
            }
        }

        // This is a little "checksum" to be sure we really tested what we were expecting.
        // it breaks every time you add a file, so you have to update it.
        assertEquals(5, dirCount)
        // this is the number of files not named original.*
        assertEquals(15, fileCount)
    }
}

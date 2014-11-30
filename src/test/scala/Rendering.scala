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
import org.spongepowered.config.ConfigFactory
import org.spongepowered.config.ConfigRenderOptions

object RenderExample extends App {
    val formatted = args.contains("--formatted")
    val originComments = args.contains("--origin-comments")
    val comments = args.contains("--comments")
    val hocon = args.contains("--hocon")
    val options = ConfigRenderOptions.defaults()
        .setFormatted(formatted)
        .setOriginComments(originComments)
        .setComments(comments)
        .setJson(!hocon)

    def render(what: String) {
        val conf = ConfigFactory.defaultOverrides()
            .withFallback(ConfigFactory.parseResourcesAnySyntax(classOf[ConfigFactory], "/" + what))
            .withFallback(ConfigFactory.defaultReference())

        println("=== BEGIN UNRESOLVED toString() " + what)
        print(conf.root.toString())
        println("=== END UNRESOLVED toString() " + what)

        println("=== BEGIN UNRESOLVED " + what)
        print(conf.root.render(options))
        println("=== END UNRESOLVED " + what)

        println("=== BEGIN RESOLVED " + what)
        print(conf.resolve().root.render(options))
        println("=== END RESOLVED " + what)
    }

    render("test01")
    render("test06")
    render("test05")
}

object RenderOptions extends App {
    val conf = ConfigFactory.parseString(
        """
            foo=[1,2,3]
            # comment1
            bar {
                a = 42
                #comment2
                b = { c = "hello", d = true }
                #    comment3
                e = ${something}
                f = {}
            }
""")

    // ah, efficiency
    def allBooleanLists(length: Int): Seq[Seq[Boolean]] = {
        if (length == 0) {
            Seq(Nil)
        } else {
            val tails = allBooleanLists(length - 1)
            (tails map { false +: _ }) ++ (tails map { true +: _ })
        }
    }

    val rendered =
        allBooleanLists(4).foldLeft(0) { (count, values) =>
            val formatted = values(0)
            val originComments = values(1)
            val comments = values(2)
            val json = values(3)

            val options = ConfigRenderOptions.defaults()
                .setFormatted(formatted)
                .setOriginComments(originComments)
                .setComments(comments)
                .setJson(json)
            val renderSpec = options.toString.replace("ConfigRenderOptions", "")
            println("=== " + count + " RENDER WITH " + renderSpec + "===")
            print(conf.root.render(options))
            println("=== " + count + " END RENDER WITH " + renderSpec + "===")
            count + 1
        }
    println("Rendered " + rendered + " option combinations")
}

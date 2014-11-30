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
import org.spongepowered.config.Config
import org.spongepowered.config.ConfigFactory
import org.spongepowered.config.ConfigException
import java.util.concurrent.TimeUnit
import scala.annotation.tailrec

object Util {
    @tailrec
    def timeHelper(body: () => Unit, iterations: Int, retried: Boolean): Double = {
        // warm up
        for (i <- 1 to Math.max(20, iterations / 10)) {
            body()
        }

        val start = System.nanoTime()
        for (i <- 1 to iterations) {
            body()
        }
        val end = System.nanoTime()

        val elapsed = end - start

        val nanosInMillisecond = 1000000L

        if (elapsed < (1000 * nanosInMillisecond)) {
            System.err.println(s"Total time for $iterations was less than a second; trying with more iterations")
            timeHelper(body, iterations * 10, true)
        } else {
            if (retried)
                System.out.println(s"with $iterations we got a long enough sample (${elapsed.toDouble / nanosInMillisecond}ms)")
            (elapsed.toDouble / iterations) / nanosInMillisecond
        }
    }

    def time(body: () => Unit, iterations: Int): Double = {
        timeHelper(body, iterations, false)
    }

    def loop(args: Seq[String], body: () => Unit) {
        if (args.contains("-loop")) {
            println("looping; ctrl+C to escape")
            while (true) {
                body()
            }
        }
    }
}

object FileLoad extends App {
    def task() {
        val conf = ConfigFactory.load("test04")
        if (!"2.0-SNAPSHOT".equals(conf.getString("akka.version"))) {
            throw new Exception("broken file load")
        }
    }

    val ms = Util.time(task, 4000)
    println("file load: " + ms + "ms")

    Util.loop(args, task)
}

object Resolve extends App {
    val conf = ConfigFactory.load("test02")

    def task() {
        conf.resolve()
        if (conf.getInt("103_a") != 103) {
            throw new Exception("broken file load")
        }
    }

    val ms = Util.time(task, 3000000)
    println("resolve: " + ms + "ms")

    Util.loop(args, task)
}

object GetExistingPath extends App {
    val conf = ConfigFactory.parseString("aaaaa.bbbbb.ccccc.d=42").resolve()

    def task() {
        if (conf.getInt("aaaaa.bbbbb.ccccc.d") != 42) {
            throw new Exception("broken get")
        }
    }

    val ms = Util.time(task, 2000000)
    println("GetExistingPath: " + ms + "ms")

    Util.loop(args, task)
}

object GetSeveralExistingPaths extends App {
    val conf = ConfigFactory.parseString("aaaaa { bbbbb.ccccc.d=42, qqqqq.rrrrr = 43 }, xxxxx.yyyyy.zzzzz = 44 ").resolve()

    def task() {
        if (conf.getInt("aaaaa.bbbbb.ccccc.d") != 42 ||
            conf.getInt("aaaaa.qqqqq.rrrrr") != 43 ||
            conf.getInt("xxxxx.yyyyy.zzzzz") != 44) {
            throw new Exception("broken get")
        }
    }

    val ms = Util.time(task, 5000000)
    println("GetSeveralExistingPaths: " + ms + "ms")

    Util.loop(args, task)
}

object HasPathOnMissing extends App {
    val conf = ConfigFactory.parseString("aaaaa.bbbbb.ccccc.d=42,x=10, y=11, z=12").resolve()

    def task() {
        if (conf.hasPath("aaaaa.bbbbb.ccccc.e")) {
            throw new Exception("we shouldn't have this path")
        }
    }

    val ms = Util.time(task, 20000000)
    println("HasPathOnMissing: " + ms + "ms")

    Util.loop(args, task)
}

object CatchExceptionOnMissing extends App {
    val conf = ConfigFactory.parseString("aaaaa.bbbbb.ccccc.d=42,x=10, y=11, z=12").resolve()

    def anotherStackFrame(remaining: Int)(body: () => Unit): Int = {
        if (remaining == 0) {
            body()
            123
        } else {
            42 + anotherStackFrame(remaining - 1)(body)
        }
    }

    def task() {
        try conf.getInt("aaaaa.bbbbb.ccccc.e")
        catch {
            case e: ConfigException.Missing =>
        }
    }

    anotherStackFrame(40) { () =>
        val ms = Util.time(task, 300000)
        println("CatchExceptionOnMissing: " + ms + "ms")

        Util.loop(args, task)
    }
}

/**
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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

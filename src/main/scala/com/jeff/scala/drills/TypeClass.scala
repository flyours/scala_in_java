package com.jeff.scala.drills

import java.io.{File, FileInputStream, InputStream}

/**
  * Defines an interface for things that are like files for our synchronization code.
  *
  * The type class idiom, as it exists in Scala, takes this form:
  * (1) a type class trait that acts as the accessor or utility library for a given type;
  * (2) an object with the same name as the trait (this object contains all default implementations of
  * the type class trait for various types); and
  * (3) methods with context bounds where the type trait need to be used
  */
trait FileLike[T] {
    def name(file: T): String

    def isDirectory(file: T): Boolean

    def exists(file: T): Boolean

    def children(directory: T): Seq[T]

    def parent(file: T): T

    // Creates new child of given name under this directory (throws if this is not a directory)
    def child(parent: T, name: String): T

    def mkdirs(file: T): Unit

    def content(file: T): InputStream

    // This will write a new file if it doesn't exist
    def writeContent(file: T, otherContent: InputStream): Unit
}

object FileLike {

    implicit val ioFileLike = new FileLike[File] {
        override def name(file: File) = file.getName()

        override def isDirectory(file: File) = file.isDirectory()

        override def exists(file: File) = file.exists()

        override def parent(file: File) = file.getParentFile()

        override def children(directory: File) = if(directory.exists) directory.listFiles else Seq.empty
        override def child(parent: File, name: String) = new java.io.File(parent, name)

        override def mkdirs(file: File): Unit = file.mkdirs()

        override def content(file: File) = new FileInputStream(file)

        override def writeContent(file: File, otherContent: InputStream) = {
            // TODO - Auto close input stream? yes...
            val bufferedOutput = new java.io.BufferedOutputStream(new java.io.FileOutputStream(file))
            try {
                val bufferedInput = new java.io.BufferedInputStream(otherContent)
                val buffer = new Array[Byte](512)
                var ready: Int = 0
                ready = bufferedInput.read(buffer)
                while (ready != -1) {
                    if (ready > 0) {
                        bufferedOutput.write(buffer, 0, ready)
                    }
                    ready = bufferedInput.read(buffer)
                }
            } finally {
                otherContent.close()
                bufferedOutput.close()
            }
        }
    }
}

// Utility to synchronize files
object SynchUtil {

    def synchronize[F: FileLike, T: FileLike](from: F, to: T): Unit = {
        val fromHelper = implicitly[FileLike[F]]
        val toHelper = implicitly[FileLike[T]]

        /** Synchronizes two files */
        def synchronizeFile(file1: F, file2: T): Unit = {
            println("Writing [" + fromHelper.name(file1) + "] to [" + toHelper.name(file2) + "]")
            toHelper.writeContent(file2, fromHelper.content(file1))
        }

        def synchronizeDirectory(dir1: F, dir2: T): Unit = {
            def findFile(file: F, directory: T): Option[T] = {
                val files=for {file2 <- toHelper.children(directory)
                               if fromHelper.name(file) == toHelper.name(file2)} yield file2
                files.headOption
            }

            // Iterate over all files in this directory and sync
            for (file1 <- fromHelper.children(dir1)) {
                val file2 = findFile(file1, dir2).getOrElse(toHelper.child(dir2, fromHelper.name(file1)))
                if (fromHelper.isDirectory(file1)) {
                    toHelper.mkdirs(file2) // Ensure Directory for sync
                    println("Syncing [" + fromHelper.name(file1) + "] to [" + toHelper.name(file2) + "]")
                }
                synchronize(file1, file2)
            }
        }

        if (fromHelper.isDirectory(from)) {
            synchronizeDirectory(from, to)
        } else {
            synchronizeFile(from, to)
        }
    }


}

object TypeClass extends App {
    SynchUtil.synchronize(new File("/Users/twer/benz/mme-user-service/target"), new File("/tmp/test"))
}
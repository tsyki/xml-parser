package jp.gr.java_conf.tsyki.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * ファイル、ディレクトリ操作のUtil
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2016/08/02
 */
public class FileUtil {

    /**
     * 指定のディレクトリ以下で指定の拡張子を持つファイルリストを作成します
     */
    public static List<Path> listSubordinateFile( String rootDirPath, String suffix) throws IOException {
        return listSubordinateFile( rootDirPath, suffix, new ArrayList<>());
    }

    public static List<Path> listSubordinateFile( String rootDirPath, String suffix, List<String> excludeDirNames) throws IOException {
        Path dir = Paths.get( rootDirPath);
        final List<Path> list = new ArrayList<>();
        Files.walkFileTree( dir, new FileListVisitor( list, suffix, excludeDirNames));

        return list;
    }

    private final static class FileListVisitor implements FileVisitor<Path> {

        private final List<Path> list;

        private String suffix;

        private List<String> excludeDirNames;

        private FileListVisitor( List<Path> list, String suffix, List<String> excludeDirNames) {
            this.list = list;
            this.suffix = suffix;
            this.excludeDirNames = excludeDirNames;
        }

        @Override
        public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs) throws IOException {
            String dirName = dir.getFileName().toString();
            // 除外対象のディレクトリ名だったらその先は見ない(classesなどを想定)
            if ( excludeDirNames.contains( dirName)) {
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile( Path file, BasicFileAttributes attrs) throws IOException {
            if ( file.getFileName().toString().endsWith( suffix)) {
                list.add( file);
                // System.out.println( "hit " + file.getFileName().toString());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed( Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory( Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}

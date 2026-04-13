package com.qqspeed.utils;

/**
 * 文件工具类
 */
public class FileUtils {

    /**
     * 格式化文件大小
     * @param size 文件大小（字节）
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(Long size) {
        if (size == null) {
            return "0 B";
        }

        // 如果小于1KB，显示为B
        if (size < 1024) {
            return size + " B";
        }

        // 如果小于1MB，显示为KB
        if (size < 1024 * 1024) {
            double kb = size / 1024.0;
            return String.format("%.1f KB", kb);
        }

        // 如果小于1GB，显示为MB
        if (size < 1024 * 1024 * 1024) {
            double mb = size / (1024.0 * 1024);
            return String.format("%.1f MB", mb);
        }

        // 否则显示为GB
        double gb = size / (1024.0 * 1024 * 1024);
        return String.format("%.1f GB", gb);
    }

    /**
     * 根据文件名获取文件类型
     * @param fileName 文件名
     * @return 文件类型
     */
    public static String getFileType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "unknown";
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "unknown";
        }

        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    /**
     * 根据文件类型获取分类
     * @param fileType 文件类型
     * @return 分类
     */
    public static String getCategoryByFileType(String fileType) {
        if (fileType == null) {
            return "其他";
        }

        String lowerType = fileType.toLowerCase();

        // 图片类
        if (lowerType.matches("jpg|jpeg|png|gif|bmp|webp")) {
            return "图片";
        }

        // 文档类
        if (lowerType.matches("doc|docx|xls|xlsx|ppt|pptx|pdf|txt|md")) {
            return "文档";
        }

        // 视频类
        if (lowerType.matches("mp4|avi|mkv|mov|wmv|flv|webm")) {
            return "视频";
        }

        // 音频类
        if (lowerType.matches("mp3|wav|flac|aac|ogg")) {
            return "音频";
        }

        return "其他";
    }
}
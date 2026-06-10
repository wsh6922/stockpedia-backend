package com.ktb.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class FileResponse {

    @Getter
    @AllArgsConstructor
    public static class UploadResponse {

        private String originalFileName;

        private String storeFileUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class UploadsResponse {
        private List<UploadResponse> files;
    }
}

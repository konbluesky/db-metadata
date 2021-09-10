package com.hthjsj.web.upload;

import com.hthjsj.web.WebException;

/**
 * <p> @Date : 2021/9/7 </p>
 * <p> @Project : db-metadata-server-springboot</p>
 *
 * <p> @author konbluesky </p>
 */
public class FileUploadException extends WebException {

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String messageTmpl, String... args) {
        super(messageTmpl, args);
    }
}

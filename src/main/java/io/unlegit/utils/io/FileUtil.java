package io.unlegit.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtil
{
    public static void extract(InputStream inputStream, File destination)
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream(destination))
        {
            byte[] buffer = new byte[4096];
            int read;
            
            while ((read = inputStream.read(buffer)) != -1)
                fileOutputStream.write(buffer, 0, read);
        } catch (Exception e) {}
    }
}
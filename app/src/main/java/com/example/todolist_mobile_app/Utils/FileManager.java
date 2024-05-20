package com.example.todolist_mobile_app.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.todolist_mobile_app.Data.FileModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private AppCompatActivity activity;

    public FileManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    public List<FileModel> getAllFilesForTask(int taskId) {
        List<FileModel> files = new ArrayList<>();
        File appExternalDir = activity.getExternalFilesDir(null);
        if (appExternalDir != null) {
            File taskDir = new File(appExternalDir, "task_" + taskId);
            if (taskDir.exists() && taskDir.isDirectory()) {
                File[] fileList = taskDir.listFiles();
                if (fileList != null) {
                    for (File file : fileList) {
                        files.add(new FileModel(file.getName(), file.getAbsolutePath()));
                    }
                }
            }
        }
        return files;
    }

    public void deleteAllAttachmentsForTask(int taskId) {
        File appExternalDir = activity.getExternalFilesDir(null);
        if (appExternalDir != null) {
            File taskDir = new File(appExternalDir, "task_" + taskId);
            if (taskDir.exists() && taskDir.isDirectory()) {
                deleteDirectory(taskDir);
                Toast.makeText(activity, "All attachments for task " + taskId + " have been deleted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "No attachments found for task " + taskId, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteAttachmentForTask(int taskId, String name) {
        File appExternalDir = activity.getExternalFilesDir(null);
        if (appExternalDir != null) {
            File taskDir = new File(appExternalDir, "task_" + taskId);
            if (taskDir.exists() && taskDir.isDirectory()) {
                deleteFileFromDirectory(taskDir, name);
            }
        }
    }

    private boolean deleteDirectory(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDirectory(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    private boolean deleteFileFromDirectory(File dir, String name) {
        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(name)) {
                        return file.delete();
                    }
                }
            }
        }
        return false;
    }

    public List<Uri> getUrisForTask(int taskId) {
        List<Uri> uris = new ArrayList<>();
        File appExternalDir = activity.getExternalFilesDir(null);
        if (appExternalDir != null) {
            File taskDir = new File(appExternalDir, "task_" + taskId);
            if (taskDir.exists() && taskDir.isDirectory()) {
                File[] fileList = taskDir.listFiles();
                if (fileList != null) {
                    for (File file : fileList) {
                        Uri fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
                        uris.add(fileUri);
                    }
                }
            }
        }
        return uris;
    }

    public List<FileModel> convertUrisToFileModels(List<Uri> uris, int taskId) {
        List<FileModel> fileModels = new ArrayList<>();
        for (Uri uri : uris) {
            File file = createFileFromUri(uri, taskId);
            if (file != null && file.exists()) {
                fileModels.add(new FileModel(file.getName(), file.getAbsolutePath()));
            }
        }
        return fileModels;
    }

    private File createFileFromUri(Uri uri, int taskId) {
        File appExternalDir = activity.getExternalFilesDir(null);
        if (appExternalDir != null) {
            File taskDir = new File(appExternalDir, "task_" + taskId);
            if (!taskDir.exists()) {
                taskDir.mkdirs();
            }

            String fileName = getFileName(uri);
            File file = new File(taskDir, fileName);

            try (InputStream inputStream = activity.getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(file)) {

                if (inputStream == null) return null;

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void saveFileToTaskDirectory(byte[] fileData, String fileName, int taskId) {
        File appExternalDir = activity.getExternalFilesDir(null);
        if (appExternalDir != null) {
            Log.i("ADD_TEST", "saving to: " + appExternalDir.getPath());
            File taskDir = new File(appExternalDir, "task_" + taskId);
            Log.i("ADD_TEST", "does exists: " + !taskDir.exists());
            if (!taskDir.exists()) {
                taskDir.mkdirs();
            }
            Log.i("ADD_TEST", "filename: " + fileName);
            File file = new File(taskDir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileData);
                fos.flush();
                Toast.makeText(activity, "File saved successfully in task-specific storage", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Error saving file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "External app-specific storage not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void validateRepeats(List<Uri> selectedUris, List<FileModel> allFiles) {
        for (Uri u : selectedUris) {
            for (FileModel f : allFiles) {
                Log.i("MY_URI", getFileName(u) + f.getFileName());
                if (getFileName(u).equals(f.getFileName())) {
                    selectedUris.remove(u);
                    break;
                }
            }
        }
    }

    public void deleteTemporaryFiles(List<FileModel> filesToDelete, int taskId) {
        File appExternalDir = activity.getExternalFilesDir(null);
        if (appExternalDir != null) {
            File taskDir = new File(appExternalDir, "task_" + taskId);
            if (taskDir.exists() && taskDir.isDirectory()) {
                File[] files = taskDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (hasOnDeleteList(file.getName(), filesToDelete))
                            file.delete();
                    }
                }
                if (taskDir.listFiles().length == 0)
                    taskDir.delete();
            }
        }
    }

    public boolean hasOnDeleteList(String name, List<FileModel> filesToDelete) {
        for (FileModel f : filesToDelete) {
            if (f.getFileName().equals(name)) return true;
        }
        return false;
    }

}

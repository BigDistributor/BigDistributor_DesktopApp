package com.bigdistributor.gui.wf.items;

import fiji.util.gui.GenericDialogPlus;

public class OneFileView {

    public enum FileType {
        Text, File, Folder, FileOrFolder;
    }

    private final String def;
    private final String title;
    private final String label;
    private final FileType type;

    public OneFileView(String title, String label, String def, FileType type) {
        this.title = title;
        this.label = label;
        this.def = def;
        this.type = type;
    }

    public String show() {
        final GenericDialogPlus gd = new GenericDialogPlus(title);

        switch (type) {
            case File:
                gd.addFileField(label, def, 100);
                break;
            case Folder:
                gd.addDirectoryField(label, def, 100);
                break;
            case Text:
                gd.addStringField(label, def, 100);
                break;
            case FileOrFolder:
                gd.addDirectoryOrFileField(label, def, 100);
                break;
        }


        gd.showDialog();

        if (gd.wasCanceled())
            return null;

        String file = gd.getNextString();
        return file;
    }

    public static void main(String[] args) {
        new OneFileView("AWS Task", "Task name: ", "", FileType.FileOrFolder).show();
    }
}
package com.bigdistributor.plugin;

import com.bigdistributor.gui.wf.AWSMenuView;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new AWSMenuView().setVisible(true));
    }
}

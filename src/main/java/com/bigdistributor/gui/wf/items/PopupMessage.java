package com.bigdistributor.gui.wf.items;

import javax.swing.*;

public class PopupMessage
{
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
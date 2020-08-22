package com.github.lolkilee.liverymanager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Window {

    JFrame frame;

    JPanel pnMain_panel;
    JList lsList0; //available liveries
    JButton btSelect;
    JButton btRemove;
    JList lsSelect_liveries;
    JButton btSelect_all;
    JButton btRemove_all;
    JTextField tfLivery_information_header;
    JTextArea taLivery_information_area;
    JButton btInstall_button;
    JButton btSelect_install_dir;
    JTextField tfAvailabe_text;
    JTextField tfSelected_text;
    JFileChooser fileChooser;
    JDialog warning_no_path;
    JDialog installed_liveries_msg;

    private ArrayList<LiveryContainer> available_liveries = new ArrayList<>();
    private ArrayList<LiveryContainer> selected_liveries = new ArrayList<>();
    private String[] available_liveries_name = {"No liveries detected!"};
    private String[] selected_liveries_name = {"No liveries selected!"};
    private String package_folder_path = "<empty>";

    //for available liveries
    private int selected_index_av = -1;
    //for available liveries
    private int selected_index_se = -1;

    private boolean hasUiChanged = false;


    public Window(int height, int width) {
        pnMain_panel = new JPanel();
        GridBagLayout gbMain_panel = new GridBagLayout();
        GridBagConstraints gbcMain_panel = new GridBagConstraints();
        pnMain_panel.setLayout( gbMain_panel );

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        String []dataList0 = {};
        lsList0 = new JList( dataList0 );
        lsList0.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scpList0 = new JScrollPane( lsList0 );
        gbcMain_panel.gridx = 0;
        gbcMain_panel.gridy = 1;
        gbcMain_panel.gridwidth = 8;
        gbcMain_panel.gridheight = 11;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 1;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( scpList0, gbcMain_panel );
        pnMain_panel.add( scpList0 );

        lsList0.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(lsList0.getSelectedIndex() > -1)
                selected_index_av = lsList0.getSelectedIndex();
                System.out.println("index changed to: " + selected_index_av);
            }
        });

        btSelect = new JButton( "Select livery →"  );
        gbcMain_panel.gridx = 8;
        gbcMain_panel.gridy = 1;
        gbcMain_panel.gridwidth = 3;
        gbcMain_panel.gridheight = 1;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( btSelect, gbcMain_panel );
        pnMain_panel.add( btSelect );

        btSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed the select button");
                int index = selected_index_av;
                if(index > -1)
                addSelectedLivery(available_liveries.get(0));
                updateSelection();
            }
        });

        btRemove = new JButton( "← Remove livery"  );
        gbcMain_panel.gridx = 8;
        gbcMain_panel.gridy = 4;
        gbcMain_panel.gridwidth = 3;
        gbcMain_panel.gridheight = 1;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( btRemove, gbcMain_panel );
        pnMain_panel.add( btRemove );

        btRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed the remove button");
                int index = selected_index_se;
                if(index > -1)
                removeSelectedLivery(available_liveries.get(index));
                updateSelection();
            }
        });

        String []dataSelect_liveries = {};
        lsSelect_liveries = new JList( dataSelect_liveries );
        lsSelect_liveries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scpSelect_liveries = new JScrollPane( lsSelect_liveries );
        gbcMain_panel.gridx = 11;
        gbcMain_panel.gridy = 1;
        gbcMain_panel.gridwidth = 9;
        gbcMain_panel.gridheight = 11;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 1;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( scpSelect_liveries, gbcMain_panel );
        pnMain_panel.add( scpSelect_liveries );
        lsSelect_liveries.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(lsSelect_liveries.getSelectedIndex() > -1)
                selected_index_se = lsSelect_liveries.getSelectedIndex();
                System.out.println("index changed to: " + selected_index_se);
            }
        });

        btSelect_all = new JButton( "Select All →"  );
        gbcMain_panel.gridx = 8;
        gbcMain_panel.gridy = 2;
        gbcMain_panel.gridwidth = 3;
        gbcMain_panel.gridheight = 1;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( btSelect_all, gbcMain_panel );
        pnMain_panel.add( btSelect_all );
        btSelect_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(LiveryContainer container : available_liveries) {
                    addSelectedLivery(container);
                }
            }
        });

        btRemove_all = new JButton( "← Remove All"  );
        gbcMain_panel.gridx = 8;
        gbcMain_panel.gridy = 3;
        gbcMain_panel.gridwidth = 3;
        gbcMain_panel.gridheight = 1;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( btRemove_all, gbcMain_panel );
        pnMain_panel.add( btRemove_all );
        btRemove_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(LiveryContainer container : available_liveries) {
                    removeSelectedLivery(container);
                }
            }
        });

        tfLivery_information_header = new JTextField("Livery information:");
        tfLivery_information_header.setEditable(false);
        gbcMain_panel.gridx = 0;
        gbcMain_panel.gridy = 12;
        gbcMain_panel.gridwidth = 8;
        gbcMain_panel.gridheight = 2;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( tfLivery_information_header, gbcMain_panel );
        pnMain_panel.add( tfLivery_information_header );

        taLivery_information_area = new JTextArea(2,10);
        taLivery_information_area.setEditable(false);
        JScrollPane scpLivery_information_area = new JScrollPane( taLivery_information_area );
        gbcMain_panel.gridx = 0;
        gbcMain_panel.gridy = 14;
        gbcMain_panel.gridwidth = 20;
        gbcMain_panel.gridheight = 6;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 1;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( scpLivery_information_area, gbcMain_panel );
        pnMain_panel.add( scpLivery_information_area );

        tfAvailabe_text = new JTextField("Available liveries");
        tfAvailabe_text.setEditable(false);
        gbcMain_panel.gridx = 0;
        gbcMain_panel.gridy = 0;
        gbcMain_panel.gridwidth = 8;
        gbcMain_panel.gridheight = 1;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( tfAvailabe_text, gbcMain_panel );
        pnMain_panel.add( tfAvailabe_text );

        tfSelected_text = new JTextField("Selected liveries");
        tfSelected_text.setEditable(false);
        gbcMain_panel.gridx = 11;
        gbcMain_panel.gridy = 0;
        gbcMain_panel.gridwidth = 9;
        gbcMain_panel.gridheight = 1;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( tfSelected_text, gbcMain_panel );
        pnMain_panel.add( tfSelected_text );

        btSelect_install_dir = new JButton( "Select Installation Folder"  );
        gbcMain_panel.gridx = 8;
        gbcMain_panel.gridy = 11;
        gbcMain_panel.gridwidth = 3;
        gbcMain_panel.gridheight = 1;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( btSelect_install_dir, gbcMain_panel );
        pnMain_panel.add( btSelect_install_dir );

        btSelect_install_dir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(frame);
                if(result == JFileChooser.APPROVE_OPTION) {
                    try {
                        package_folder_path = fileChooser.getSelectedFile().getCanonicalPath();
                        LiveryManager.install_folder_path = package_folder_path;
                        System.out.println("install path: " + LiveryManager.install_folder_path);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        btInstall_button = new JButton( "Install Liveries"  );
        gbcMain_panel.gridx = 8;
        gbcMain_panel.gridy = 9;
        gbcMain_panel.gridwidth = 3;
        gbcMain_panel.gridheight = 2;
        gbcMain_panel.fill = GridBagConstraints.BOTH;
        gbcMain_panel.weightx = 1;
        gbcMain_panel.weighty = 0;
        gbcMain_panel.anchor = GridBagConstraints.NORTH;
        gbMain_panel.setConstraints( btInstall_button, gbcMain_panel );
        pnMain_panel.add( btInstall_button );

        btInstall_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!package_folder_path.equalsIgnoreCase("<empty>")) {
                    System.out.println("Installing liveries to: " + package_folder_path);
                    try {
                        Main.liveryManager.installFiles(package_folder_path, selected_liveries);
                        JPanel content = new JPanel();
                        JTextField msg = new JTextField();
                        msg.setEditable(false);
                        msg.setText("Successfully installed " + selected_liveries.size() + " liveries");
                        content.add(msg);
                        installed_liveries_msg.setContentPane(content);
                        installed_liveries_msg = new JDialog(frame, "Installed to: " + package_folder_path);
                        installed_liveries_msg.setSize(600, 100);
                        installed_liveries_msg.setVisible(true);
                        installed_liveries_msg.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                        installed_liveries_msg.setLocationRelativeTo(null);

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    warning_no_path = new JDialog(frame, "Warning, no install folder selected!");
                    warning_no_path.setSize(300, 50);
                    warning_no_path.setVisible(true);
                    warning_no_path.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    warning_no_path.setLocationRelativeTo(null);
                }
            }
        });

        frame = new JFrame("Megapack livery manager");
        frame.setLocationRelativeTo(null);
        frame.setContentPane(pnMain_panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
    }

    public void update() {
        lsList0.setListData(available_liveries_name);
        lsSelect_liveries.setListData(selected_liveries_name);

        updateList(lsList0, new ArrayList(Arrays.asList(available_liveries_name)));
        updateList(lsSelect_liveries, new ArrayList(Arrays.asList(selected_liveries_name)));

        if(hasUiChanged) {
            lsList0.updateUI();
            lsSelect_liveries.updateUI();
            hasUiChanged = false;
        }
    }

    public void updateList(JList list, ArrayList objs) {
        list.setListData(objs.toArray());
    }

    public void setAvailableLiveries(ArrayList<ArrayList<LiveryContainer>> containerArrayList) {
        available_liveries.clear();
        for(ArrayList<LiveryContainer> liveryContainers: containerArrayList) {
            for(LiveryContainer container : liveryContainers) {
                available_liveries.add(container);
            }
        }

        updateSelection();
    }

    public void updateSelection() {

        //Update available liveries
        available_liveries_name = new String[available_liveries.size()];
        for(LiveryContainer liveryContainer : available_liveries) {
            int i = available_liveries.indexOf(liveryContainer);
            String s = liveryContainer.getPlaneName() + " | " + liveryContainer.getLiveryName();
            s = s.replace("_", " ");
            available_liveries_name[i] = s;
        }

        //Update selected liveries
        selected_liveries_name = new String[selected_liveries.size()];
        for(LiveryContainer liveryContainer : selected_liveries) {
            int i = selected_liveries.indexOf(liveryContainer);
            String s = liveryContainer.getPlaneName() + " | " + liveryContainer.getLiveryName();
            s = s.replace("_", " ");
            selected_liveries_name[i] = s;
        }

        hasUiChanged = true;
    }

    public void addSelectedLivery(LiveryContainer container) {
        System.out.println("Adding livery: " + container.getLiveryName());
        if(!selected_liveries.contains(container)) {
            selected_liveries.add(container);
        }

        updateSelection();
    }

    public void removeSelectedLivery(LiveryContainer container) {
        System.out.println("Removing livery: " + container.getLiveryName());
        if(selected_liveries.contains(container)) {
            selected_liveries.remove(container);
        }

        updateSelection();
    }
}

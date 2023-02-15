package me.remie.ikov.barrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.remie.ikov.barrows.data.BarrowsSettings;
import me.remie.ikov.barrows.types.FoodTypes;
import me.remie.ikov.barrows.types.MagicSpell;
import me.remie.ikov.barrows.types.OffensivePrayer;
import simple.api.ClientContext;
import simple.api.filters.SimpleSkills;
import simple.api.wrappers.SimpleItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reminisce on Feb 12, 2023 at 4:11 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class BarrowsFrame extends JFrame {

    private final ClientContext ctx;
    private final BarrowsScript script;

    private JComboBox<FoodTypes> foodComboBox;
    private JSpinner eatHealthSpinner;
    private JSpinner drinkPrayerSpinner;
    private JSpinner presetSpinner;
    private JComboBox<OffensivePrayer> primaryOffensivePrayerComboBox;
    private JComboBox<OffensivePrayer> secondaryOffensivePrayerComboBox;
    private final DefaultListModel<String> primaryEquipmentListModel = new DefaultListModel<>();
    private final DefaultListModel<String> secondaryEquipmentListModel = new DefaultListModel<>();

    private JComboBox<MagicSpell> magicSpellComboBox;

    public BarrowsFrame(final BarrowsScript script) {
        this.script = script;
        this.ctx = script.ctx;
        initComponents();
    }

    private void initComponents() {
        this.setTitle("RBarrows - Ikov");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(ctx.mouse.getComponent());

        JTabbedPane tabbedPane = new JTabbedPane();
        this.add(tabbedPane);

        JPanel generalPanel = new JPanel();
        tabbedPane.addTab("General", generalPanel);

        generalPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

        generalPanel.setLayout(new GridLayout(8, 2));

        JLabel foodLabel = new JLabel("Food: ");
        generalPanel.add(foodLabel);

        foodComboBox = new JComboBox<>(FoodTypes.values());
        generalPanel.add(foodComboBox);

        JLabel eatHealthLabel = new JLabel("Eat At Health: ");
        generalPanel.add(eatHealthLabel);

        eatHealthSpinner = new JSpinner();
        eatHealthSpinner.setValue(50);
        eatHealthSpinner.setModel(new SpinnerNumberModel(50, 10, 90, 1));
        generalPanel.add(eatHealthSpinner);

        JLabel drinkPrayerLabel = new JLabel("Drink Prayer At: ");
        generalPanel.add(drinkPrayerLabel);

        drinkPrayerSpinner = new JSpinner();
        drinkPrayerSpinner.setValue(20);
        drinkPrayerSpinner.setModel(new SpinnerNumberModel(20, 10, 90, 1));
        generalPanel.add(drinkPrayerSpinner);

        JLabel magicSpellLabel = new JLabel("Magic Spell: ");
        generalPanel.add(magicSpellLabel);

        magicSpellComboBox = new JComboBox<>(MagicSpell.values());
        generalPanel.add(magicSpellComboBox);

        JLabel presetLabel = new JLabel("Preset id (1-10): ");
        generalPanel.add(presetLabel);

        presetSpinner = new JSpinner();
        presetSpinner.setValue(1);
        presetSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        generalPanel.add(presetSpinner);

        JLabel primaryPrayerLabel = new JLabel("Primary Equipment Prayer: ");
        generalPanel.add(primaryPrayerLabel);

        primaryOffensivePrayerComboBox = new JComboBox<>(OffensivePrayer.values());
        generalPanel.add(primaryOffensivePrayerComboBox);

        JLabel secondaryPrayerLabel = new JLabel("Secondary Equipment Prayer: ");
        generalPanel.add(secondaryPrayerLabel);

        secondaryOffensivePrayerComboBox = new JComboBox<>(OffensivePrayer.values());
        generalPanel.add(secondaryOffensivePrayerComboBox);

        JPanel primaryEquipmentPanel = new JPanel();
        tabbedPane.addTab("Primary Equipment", primaryEquipmentPanel);
        primaryEquipmentPanel.setLayout(new BorderLayout());

        JList<String> primaryEquipmentList = new JList<>(primaryEquipmentListModel);
        JScrollPane primaryEquipmentListScrollPane = new JScrollPane(primaryEquipmentList);
        primaryEquipmentPanel.add(primaryEquipmentListScrollPane, BorderLayout.CENTER);

        JButton primaryLoadCurrentGearButton = new JButton("Load Current Gear");
        primaryLoadCurrentGearButton.addActionListener(e -> loadCurrentEquipment(primaryEquipmentListModel));
        primaryEquipmentPanel.add(primaryLoadCurrentGearButton, BorderLayout.EAST);

        JPanel secondaryEquipmentPanel = new JPanel();
        tabbedPane.addTab("Secondary Equipment", secondaryEquipmentPanel);
        secondaryEquipmentPanel.setLayout(new BorderLayout());

        JList<String> secondaryEquipmentList = new JList<>(secondaryEquipmentListModel);
        JScrollPane secondaryEquipmentListScrollPane = new JScrollPane(secondaryEquipmentList);
        secondaryEquipmentPanel.add(secondaryEquipmentListScrollPane, BorderLayout.CENTER);

        JButton secondaryLoadCurrentGearButton = new JButton("Load Current Gear");
        secondaryLoadCurrentGearButton.addActionListener(e -> loadCurrentEquipment(secondaryEquipmentListModel));
        secondaryEquipmentPanel.add(secondaryLoadCurrentGearButton, BorderLayout.EAST);

        JPanel infoPanel = new JPanel();
        tabbedPane.addTab("Info", infoPanel);
        infoPanel.setLayout(new BorderLayout());

        JScrollPane infoScrollPane = new JScrollPane();
        infoPanel.add(infoScrollPane, BorderLayout.CENTER);

        JTextPane infoTextArea = new JTextPane();
        infoTextArea.setContentType("text/html");
        infoTextArea.setEditable(false);
        infoTextArea.setPreferredSize(new Dimension(500, 200));
        infoTextArea.setText("<html><body style='background-color: #333333; color: white; font-family: Arial, sans-serif;'>" +
                "<h2 style='margin: 10px 0;'>General Settings</h2>" +
                "<p style='margin: 10px 0;'>The script will automatically drink any type of prayer potion found in your inventory. You must select a food type and set the health value at which the food should be eaten. A prayer level of 43 is required to use all protection prayers, and the script does not handle curse prayers. This can be added upon request. Additionally, you must have a spade in your inventory as tool belts will not work for Barrows.</p>" +
                "<h2 style='margin: 10px 0;'>Equipment</h2>" +
                "<p style='margin: 10px 0;'>The 'Primary Equipment' tab is used to select gear for killing the melee Barrows brothers, and it is recommended to use magic equipment since they are all weak to magic. The 'Secondary Equipment' tab is used to kill Ahrim and Karil, who are weak to magic and ranged, respectively. Both tabs allow you to load your current gear by clicking the 'Load Current Gear' button.</p>" +
                "</body></html>");
        infoTextArea.setCaretPosition(0);
        infoScrollPane.setViewportView(infoTextArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        JPanel buttonSubPanel1 = new JPanel();
        buttonSubPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton saveButton = new JButton("Save settings");
        saveButton.addActionListener(e -> saveSettings());
        buttonSubPanel1.add(saveButton);
        JButton loadButton = new JButton("Load settings");
        loadButton.addActionListener(e -> loadSettings());
        buttonSubPanel1.add(loadButton);
        buttonPanel.add(buttonSubPanel1, BorderLayout.NORTH);

        JPanel buttonSubPanel2 = new JPanel();
        buttonSubPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        JButton startScriptButton = new JButton("Start Script");
        startScriptButton.addActionListener(e -> startScript());
        buttonSubPanel2.add(startScriptButton);
        buttonPanel.add(buttonSubPanel2, BorderLayout.SOUTH);

        this.add(buttonPanel, BorderLayout.SOUTH);

        pack();
        this.setVisible(true);
    }

    /**
     *
     * @return
     */
    private List<String> getEquipment() {
        final List<String> equipment = new ArrayList<>();
        for (SimpleItem item : ctx.equipment.populate()) {
            if (item == null) {
                continue;
            }
            equipment.add(item.getName());
        }
        return equipment;
    }

    private void loadCurrentEquipment(DefaultListModel<String> model) {
        final List<String> equipment = getEquipment();
        model.clear();
        for (String item : equipment) {
            model.addElement(item);
        }
    }

    public void startScript() {
        if (ctx.skills.getRealLevel(SimpleSkills.Skill.PRAYER) < 43) {
            JOptionPane.showMessageDialog(this, "You must have a prayer level of 43 to use all protection prayers.");
            return;
        }

        if (foodComboBox.getSelectedItem() == FoodTypes.NONE) {
            JOptionPane.showMessageDialog(this, "You must select a food type.");
            return;
        }

        if (magicSpellComboBox.getSelectedItem() == MagicSpell.NONE) {
            int choice = JOptionPane.showConfirmDialog(this, "You have chosen not to use a magic spell. This is not recommended.\n" +
                    "Please note that the script will not automatically cast spells or refill quivers with this setting.\n" +
                    "Do you want to continue with this setting?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        final int presetIndex = (int) presetSpinner.getValue();
        if (presetIndex < 1 || presetIndex > 10) {
            JOptionPane.showMessageDialog(this, "Preset index must be between 1 and 10.");
            return;
        }

        if (primaryEquipmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "You must setup primary equipment.");
            return;
        }

        if (secondaryEquipmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "You must setup secondary equipment.");
            return;
        }

        final OffensivePrayer primaryPrayer = (OffensivePrayer) primaryOffensivePrayerComboBox.getSelectedItem();
        if (primaryPrayer == null) {
            JOptionPane.showMessageDialog(this, "You must select a primary offensive prayer.");
            return;
        }
        if (primaryPrayer != OffensivePrayer.NONE) {
            if (ctx.skills.getRealLevel(SimpleSkills.Skill.PRAYER) < primaryPrayer.getPrayer().getLevel()) {
                JOptionPane.showMessageDialog(this, "You must have a prayer level of " + primaryPrayer.getPrayer().getLevel() + " to use " + primaryPrayer.getName() + ".");
                return;
            }
        }

        final OffensivePrayer secondaryPrayer = (OffensivePrayer) secondaryOffensivePrayerComboBox.getSelectedItem();
        if (secondaryPrayer == null) {
            JOptionPane.showMessageDialog(this, "You must select a secondary offensive prayer.");
            return;
        }
        if (secondaryPrayer != OffensivePrayer.NONE) {
            if (ctx.skills.getRealLevel(SimpleSkills.Skill.PRAYER) < secondaryPrayer.getPrayer().getLevel()) {
                JOptionPane.showMessageDialog(this, "You must have a prayer level of " + secondaryPrayer.getPrayer().getLevel() + " to use " + secondaryPrayer.getName() + ".");
                return;
            }
        }

        final boolean cursesEnabled = primaryPrayer.ordinal() >= 7 || secondaryPrayer.ordinal() >= 7;
        script.getState().setCursesPrayerEnabled(cursesEnabled);

        script.startScript(getSettings());
    }

    /**
     * Creates a new settings object from the GUI settings.
     * @return the settings object
     */
    private BarrowsSettings getSettings() {
        try {
            final String[] primaryEquipment = new String[primaryEquipmentListModel.getSize()];
            for (int i = 0; i < primaryEquipmentListModel.getSize(); i++) {
                primaryEquipment[i] = primaryEquipmentListModel.getElementAt(i);

            }
            final String[] secondaryEquipment = new String[secondaryEquipmentListModel.getSize()];
            for (int i = 0; i < secondaryEquipmentListModel.getSize(); i++) {
                secondaryEquipment[i] = secondaryEquipmentListModel.getElementAt(i);
            }
            final BarrowsSettings settings = new BarrowsSettings(
                    (FoodTypes) foodComboBox.getSelectedItem(),
                    (int) eatHealthSpinner.getValue(),
                    (int) drinkPrayerSpinner.getValue(),
                    (MagicSpell) magicSpellComboBox.getSelectedItem(),
                    (OffensivePrayer) primaryOffensivePrayerComboBox.getSelectedItem(),
                    (OffensivePrayer) secondaryOffensivePrayerComboBox.getSelectedItem(),
                    (int) presetSpinner.getValue(),
                    primaryEquipment,
                    secondaryEquipment);
            return settings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Opens a file dialog to load settings from a json file
     */
    private void saveSettings() {
        try {
            final BarrowsSettings settings = getSettings();

            final File scriptDirectory = script.getStorageDirectory();

            //Open a save file dialog that only allows json files to be selected
            final JFileChooser fileChooser = new JFileChooser(scriptDirectory);
            fileChooser.setDialogTitle("Save settings");
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".json")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
                }
                final Gson gson = new GsonBuilder().setPrettyPrinting().create();
                final String json = gson.toJson(settings);

                final FileWriter writer = new FileWriter(selectedFile);
                writer.write(json);
                writer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads settings from a json file
     */
    private void loadSettings() {
        try {
            final File scriptDirectory = script.getStorageDirectory();

            final JFileChooser fileChooser = new JFileChooser(scriptDirectory);
            fileChooser.setDialogTitle("Load settings");
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".json")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
                }
                final Gson gson = new GsonBuilder().setPrettyPrinting().create();
                final BarrowsSettings settings = gson.fromJson(new FileReader(selectedFile), BarrowsSettings.class);

                foodComboBox.setSelectedItem(settings.getFoodType());
                eatHealthSpinner.setValue(settings.getEatHealth());
                drinkPrayerSpinner.setValue(settings.getDrinkPrayer());
                magicSpellComboBox.setSelectedItem(settings.getMagicSpell());
                primaryOffensivePrayerComboBox.setSelectedItem(settings.getPrimaryOffensivePrayer());
                secondaryOffensivePrayerComboBox.setSelectedItem(settings.getSecondaryOffensivePrayer());
                presetSpinner.setValue(settings.getPresetId());

                primaryEquipmentListModel.clear();
                for (String item : settings.getPrimaryEquipment()) {
                    primaryEquipmentListModel.addElement(item);
                }
                secondaryEquipmentListModel.clear();
                for (String item : settings.getSecondaryEquipment()) {
                    secondaryEquipmentListModel.addElement(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the script and disposes the GUI when the window is closed
     */
    @Override
    public void dispose() {
        super.dispose();
        ctx.stopScript();
    }

}

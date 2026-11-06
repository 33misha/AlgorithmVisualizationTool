package avt;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final Color BACKGROUND = new Color(18, 24, 38);
    private static final Color PANEL = new Color(29, 38, 56);
    private static final Color ACCENT = new Color(75, 219, 190);
    private static final Color TEXT = new Color(238, 243, 250);
    private static final Color MUTED = new Color(166, 180, 198);

    private final JFrame window = new JFrame("Algorithm Visualization Tool");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screens = new JPanel(cardLayout);
    private final JLabel previewTitle = new JLabel("", SwingConstants.CENTER);
    private final JLabel previewDescription = new JLabel("", SwingConstants.CENTER);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().show());
    }

    private void show() {
        screens.add(buildWelcomeScreen(), "welcome");
        screens.add(buildMenuScreen(), "menu");
        screens.add(buildPreviewScreen(), "preview");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(760, 560));
        window.setSize(1000, 700);
        window.setLocationRelativeTo(null);
        window.setContentPane(screens);
        cardLayout.show(screens, "welcome");
        window.setVisible(true);
    }

    private JPanel buildWelcomeScreen() {
        JPanel page = pagePanel();
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel eyebrow = label("LEARN BY WATCHING", 14, ACCENT, true);
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = label("<html><div style='text-align:center'>Algorithm<br>Visualization Tool</div></html>",
                38, TEXT, true);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel subtitle = label(
                "Explore algorithms and data structures, one step at a time.",
                16, MUTED, false);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JButton start = button("Start exploring");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(event -> cardLayout.show(screens, "menu"));

        content.add(eyebrow);
        content.add(Box.createVerticalStrut(18));
        content.add(title);
        content.add(Box.createVerticalStrut(14));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(32));
        content.add(start);
        page.add(content);
        return page;
    }

    private JPanel buildMenuScreen() {
        JPanel page = pagePanel();
        page.setLayout(new BorderLayout(24, 24));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = label("Choose a visualization", 30, TEXT, true);
        JLabel subtitle = label("Select a topic to open its demo.", 15, MUTED, false);
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        JPanel choices = new JPanel(new GridLayout(0, 2, 14, 14));
        choices.setOpaque(false);
        addChoice(choices, "Bubble Sort", "Chapter 3 · Simple sorting");
        addChoice(choices, "Selection Sort", "Chapter 3 · Simple sorting");
        addChoice(choices, "Stack", "Chapter 4 · Stacks and queues");
        addChoice(choices, "Queue", "Chapter 4 · Stacks and queues");
        addChoice(choices, "Linked List", "Chapter 5 · Linked lists");
        addChoice(choices, "Merge Sort", "Chapter 6 · Recursion");
        addChoice(choices, "Quick Sort", "Chapter 7 · Advanced sorting");

        JButton back = button("Back");
        back.addActionListener(event -> cardLayout.show(screens, "welcome"));

        page.add(header, BorderLayout.NORTH);
        page.add(choices, BorderLayout.CENTER);
        page.add(back, BorderLayout.SOUTH);
        return page;
    }

    private void addChoice(JPanel choices, String name, String chapter) {
        JButton choice = new JButton("<html><div style='text-align:left'><b>" + name
                + "</b><br><span style='color:#a6b4c6'>" + chapter + "</span></div></html>");
        choice.setHorizontalAlignment(SwingConstants.LEFT);
        choice.setBackground(PANEL);
        choice.setForeground(TEXT);
        choice.setFont(new Font("SansSerif", Font.PLAIN, 16));
        choice.setFocusPainted(false);
        choice.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 69, 91)),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        choice.addActionListener(event -> openPreview(name, chapter));
        choices.add(choice);
    }

    private JPanel buildPreviewScreen() {
        JPanel page = pagePanel();
        page.setLayout(new BorderLayout(20, 20));

        JButton back = button("Back to visualizations");
        back.addActionListener(event -> cardLayout.show(screens, "menu"));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        previewTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        previewTitle.setForeground(TEXT);
        previewDescription.setFont(new Font("SansSerif", Font.PLAIN, 16));
        previewDescription.setForeground(MUTED);
        JLabel placeholder = label("Step-by-step demo will be added here.", 18, ACCENT, true);
        placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(Box.createVerticalGlue());
        center.add(previewTitle);
        center.add(Box.createVerticalStrut(10));
        center.add(previewDescription);
        center.add(Box.createVerticalStrut(36));
        center.add(placeholder);
        center.add(Box.createVerticalGlue());

        page.add(back, BorderLayout.NORTH);
        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private void openPreview(String name, String chapter) {
        previewTitle.setText(name);
        previewDescription.setText(chapter + " · Visualization coming soon");
        cardLayout.show(screens, "preview");
    }

    private JPanel pagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));
        return panel;
    }

    private JLabel label(String text, int size, Color color, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    private JButton button(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(BACKGROUND);
        button.setBackground(ACCENT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        return button;
    }
}

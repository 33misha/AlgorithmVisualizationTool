package avt;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/** Connects a standard Java console program to the Swing console page. */
class ConsolePanel extends JPanel {
    private final JTextArea output = new JTextArea();
    private final JTextField input = new JTextField();
    private final JButton send = new JButton("Send");
    private final JButton restart = new JButton("Restart");
    private final JLabel status = new JLabel("Ready");
    private SwingWorker<Integer, String> session;
    private volatile Process process;
    private BufferedWriter inputWriter;

    ConsolePanel(Runnable onBack) {
        super(new BorderLayout(12, 12));
        Color background = new Color(18, 24, 38);
        Color text = new Color(238, 243, 250);
        setBackground(background);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JButton back = new JButton("Back");
        back.addActionListener(event -> onBack.run());
        restart.addActionListener(event -> startSession());
        JPanel navigation = new JPanel(new BorderLayout(12, 0));
        navigation.setOpaque(false);
        status.setForeground(text);
        status.setHorizontalAlignment(SwingConstants.CENTER);
        navigation.add(back, BorderLayout.WEST);
        navigation.add(status, BorderLayout.CENTER);
        navigation.add(restart, BorderLayout.EAST);

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        output.setBackground(background);
        output.setForeground(text);
        output.setMargin(new Insets(12, 12, 12, 12));
        output.getAccessibleContext().setAccessibleName("Console output");

        input.setFont(output.getFont());
        input.setBackground(background);
        input.setForeground(text);
        input.setCaretColor(text);
        input.getAccessibleContext().setAccessibleName("Console input");
        input.addActionListener(event -> submitInput());
        send.addActionListener(event -> submitInput());
        JPanel commandLine = new JPanel(new BorderLayout(10, 0));
        commandLine.setOpaque(false);
        commandLine.add(input, BorderLayout.CENTER);
        commandLine.add(send, BorderLayout.EAST);
        setInputEnabled(false);

        add(navigation, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
        add(commandLine, BorderLayout.SOUTH);
    }

    void startIfNeeded() {
        if (session == null) {
            startSession();
        }
        input.requestFocusInWindow();
    }

    private void startSession() {
        output.setText("");
        input.setText("");
        status.setText("Console Playground - Running");
        restart.setEnabled(false);
        setInputEnabled(false);

        // A separate JVM preserves System.in/out for the user's console code,
        // including output from BubbleSort, without blocking Swing's UI thread.
        session = new SwingWorker<Integer, String>() {
            @Override
            protected Integer doInBackground() throws Exception {
                File bin = new File(System.getProperty("java.home"), "bin");
                File java = new File(bin, "javaw.exe");
                if (!java.isFile()) {
                    java = new File(bin, "java");
                }
                Process child = new ProcessBuilder(java.getAbsolutePath(),
                        "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8",
                        "-Dstderr.encoding=UTF-8", "-cp",
                        System.getProperty("java.class.path"), "avt.ConsolePlayground")
                        .redirectErrorStream(true).start();
                process = child;
                if (isCancelled()) {
                    child.destroyForcibly();
                    return -1;
                }
                SwingUtilities.invokeLater(() -> {
                    if (!isCancelled()) {
                        inputWriter = new BufferedWriter(new OutputStreamWriter(
                                child.getOutputStream(), StandardCharsets.UTF_8));
                        setInputEnabled(true);
                        input.requestFocusInWindow();
                    }
                });
                try (Reader reader = new InputStreamReader(
                        child.getInputStream(), StandardCharsets.UTF_8)) {
                    char[] buffer = new char[1024];
                    int count;
                    while ((count = reader.read(buffer)) != -1) {
                        publish(new String(buffer, 0, count));
                    }
                } finally {
                    if (child.isAlive()) {
                        child.destroy();
                    }
                }
                return child.waitFor();
            }

            @Override
            protected void process(List<String> chunks) {
                append(String.join("", chunks));
            }

            @Override
            protected void done() {
                inputWriter = null;
                setInputEnabled(false);
                restart.setEnabled(true);
                try {
                    int exitCode = get();
                    status.setText(exitCode == 0 ? "Finished - Restart to run again"
                            : "Console exited with code " + exitCode);
                } catch (CancellationException exception) {
                    status.setText("Console stopped");
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException exception) {
                    status.setText("Unable to run console");
                    append("\n" + exception.getCause() + "\n");
                }
            }
        };
        session.execute();
    }

    private void submitInput() {
        if (inputWriter == null) {
            return;
        }
        String line = input.getText();
        try {
            inputWriter.write(line);
            inputWriter.newLine();
            inputWriter.flush();
            append(line + "\n");
            input.setText("");
            input.requestFocusInWindow();
        } catch (IOException exception) {
            append("\nConsole input is closed.\n");
            setInputEnabled(false);
        }
    }

    private void append(String text) {
        output.append(text);
        output.setCaretPosition(output.getDocument().getLength());
    }

    private void setInputEnabled(boolean enabled) {
        input.setEnabled(enabled);
        send.setEnabled(enabled);
    }

    void stopSession() {
        if (session != null) {
            session.cancel(true);
        }
        Process child = process;
        if (child != null && child.isAlive()) {
            child.destroyForcibly();
        }
    }
}

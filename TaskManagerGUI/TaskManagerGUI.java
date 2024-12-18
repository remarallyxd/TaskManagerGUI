import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Task {
    private String title;
    private String description;
    private boolean isCompleted;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }
}

public class TaskManagerGUI extends JFrame {
    private List<Task> tasks;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;

    public TaskManagerGUI() {
        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();

        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Task Manager", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Task List Panel
        JPanel taskListPanel = new JPanel(new BorderLayout());
        taskListPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Tasks",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.DARK_GRAY
        ));
        mainPanel.add(taskListPanel, BorderLayout.CENTER);

        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskListPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JButton addButton = createStyledButton("Add Task");
        JButton viewButton = createStyledButton("View Description");
        JButton markCompletedButton = createStyledButton("Mark Completed");
        JButton deleteButton = createStyledButton("Delete Task");
        JButton exitButton = createStyledButton("Exit");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(markCompletedButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);

        // Action Listeners
        addButton.addActionListener(e -> openAddTaskWindow());
        viewButton.addActionListener(e -> viewTaskDescription());
        markCompletedButton.addActionListener(e -> markTaskAsCompleted());
        deleteButton.addActionListener(e -> deleteTask());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 30));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return button;
    }

    private void openAddTaskWindow() {
        JFrame addTaskFrame = new JFrame("Add Task");
        addTaskFrame.setSize(350, 250);
        addTaskFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionArea = new JTextArea(3, 20);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(descriptionLabel);
        panel.add(descriptionScroll);
        panel.add(saveButton);
        panel.add(cancelButton);

        addTaskFrame.add(panel);
        addTaskFrame.setVisible(true);

        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            if (!title.isEmpty()) {
                Task newTask = new Task(title, description);
                tasks.add(newTask);
                taskListModel.addElement(newTask);
                JOptionPane.showMessageDialog(this, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addTaskFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addTaskFrame, "Title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> addTaskFrame.dispose());
    }

    private void viewTaskDescription() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            JOptionPane.showMessageDialog(this,
                    "Description:\n" + selectedTask.getDescription(),
                    "Task Description: " + selectedTask.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to view its description.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markTaskAsCompleted() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            if (!selectedTask.isCompleted()) {
                selectedTask.markAsCompleted();
                taskList.repaint();
                JOptionPane.showMessageDialog(this, "Task marked as completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Task is already completed.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as completed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            taskListModel.removeElement(selectedTask);
            JOptionPane.showMessageDialog(this, "Task deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerGUI::new);
    }
}

class TaskCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Task task = (Task) value;

        label.setText((task.isCompleted() ? "[Completed] " : "[Pending] ") + task.getTitle());
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        if (task.isCompleted()) {
            label.setForeground(new Color(34, 139, 34)); // Green for completed
        } else {
            label.setForeground(new Color(30, 144, 255)); // Blue for pending
        }

        return label;
    }
}
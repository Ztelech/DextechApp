import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

public class DextechApp extends JFrame {

    // --- CONFIGURACIÓN DE ESTILO ---
    private final Color DARK_BLUE = new Color(15, 23, 42);
    private final Color ACCENT_GREEN = new Color(34, 197, 94);
    private final Color TEXT_GRAY = new Color(148, 163, 184);

    private JPanel mainPanel;
    private CardLayout navegado;

    // Componentes del Cuestionario
    private JComboBox<String> cbObj, cbEnf, cbGrado, cbTiempo, cbHora;
    private JButton btnGenerar;

    public DextechApp() {
        setTitle("Dextech Math - Motor de Aprendizaje Generativo");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        navegado = new CardLayout();
        mainPanel = new JPanel(navegado);

        mainPanel.add(pantallaInicio(), "INICIO");
        mainPanel.add(pantallaCuestionario(), "CUESTIONARIO");
        mainPanel.add(pantallaDashboard(), "DASHBOARD");

        add(mainPanel);
        navegado.show(mainPanel, "INICIO");
    }

    // --- PANTALLA 1: BIENVENIDA ---
    private JPanel pantallaInicio() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(DARK_BLUE);

        JLabel logo = new JLabel("DEXTECH MATH", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);

        JButton b = new JButton("INICIAR DIAGNÓSTICO");
        estiloBoton(b, ACCENT_GREEN);
        b.addActionListener(e -> navegado.show(mainPanel, "CUESTIONARIO"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0,0,30,0);
        p.add(logo, gbc);
        gbc.gridy = 1;
        p.add(b, gbc);
        return p;
    }

    // --- PANTALLA 2: CUESTIONARIO (GRAFO DE PERFILAMIENTO V1-V5) ---
    private JPanel pantallaCuestionario() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(DARK_BLUE);
        p.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel t = new JLabel("CONFIGURACIÓN DE RUTA PERSONALIZADA", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        t.setForeground(Color.WHITE);
        p.add(t, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(5, 2, 20, 25));
        grid.setBackground(DARK_BLUE);

        // Variables del Grafo
        cbObj = new JComboBox<>(new String[]{"-- Seleccionar --", "Reforzar bases", "Aprender algo nuevo", "Preparar examen"});
        cbEnf = new JComboBox<>(new String[]{"-- Seleccionar --", "Teórico", "Práctico/Ejercicios", "Visual/Gráfico"});
        cbGrado = new JComboBox<>(new String[]{"-- Seleccionar --", "Bachillerato", "Pregrado (Ingeniería)", "Postgrado"});
        cbTiempo = new JComboBox<>(new String[]{"-- Seleccionar --", "15-30 min", "1-2 horas", "Intensivo (+3h)"});
        cbHora = new JComboBox<>(new String[]{"-- Seleccionar --", "Mañana", "Tarde", "Noche"});

        // Agregar labels y combos
        agregarFila(grid, "V1. Objetivo Principal:", cbObj);
        agregarFila(grid, "V2. Enfoque Didáctico:", cbEnf);
        agregarFila(grid, "V3. Grado de Estudio:", cbGrado);
        agregarFila(grid, "V4. Tiempo Diario:", cbTiempo);
        agregarFila(grid, "V5. Horario de Práctica:", cbHora);

        p.add(grid, BorderLayout.CENTER);

        btnGenerar = new JButton("GENERAR PENSUM GENERATIVO");
        btnGenerar.setEnabled(false); // BLOQUEADO por defecto
        estiloBoton(btnGenerar, new Color(59, 130, 246));

        // Listener para validar que todo esté respondido
        ActionListener validador = e -> validarCuestionario();
        cbObj.addActionListener(validador);
        cbEnf.addActionListener(validador);
        cbGrado.addActionListener(validador);
        cbTiempo.addActionListener(validador);
        cbHora.addActionListener(validador);

        btnGenerar.addActionListener(e -> navegado.show(mainPanel, "DASHBOARD"));
        p.add(btnGenerar, BorderLayout.SOUTH);

        return p;
    }

    // --- PANTALLA 3: DASHBOARD (ÁRBOL ESTILO ÍNDICE + CHAT) ---
    private JPanel pantallaDashboard() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBackground(DARK_BLUE);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //--- EL ÁRBOL (Syllabus dinámico) ---
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Índice: Cálculo Multivariado");

        // Módulos (Capítulos)
        DefaultMutableTreeNode m1 = new DefaultMutableTreeNode("Capítulo 1: Derivadas Parciales");
        m1.add(new DefaultMutableTreeNode("Lección 1.1: Límites en varias variables"));
        m1.add(new DefaultMutableTreeNode("Lección 1.2: Interpretación geométrica"));

        DefaultMutableTreeNode m2 = new DefaultMutableTreeNode("Capítulo 2: Integrales Múltiples");
        m2.add(new DefaultMutableTreeNode("Lección 2.1: Integrales Dobles en regiones rectangulares"));

        root.add(m1); root.add(m2);

        JTree tree = new JTree(root);
        tree.setBackground(new Color(30, 41, 59));
        tree.setForeground(Color.WHITE);
        tree.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // --- CUSTOM RENDERER (Para que parezca un libro/índice) ---
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(new Color(30, 41, 59));
        renderer.setTextNonSelectionColor(Color.WHITE);
        renderer.setClosedIcon(UIManager.getIcon("Tree.closedIcon"));
        // Nota: En un proyecto real aquí cargarías iconos de libros/hojas
        tree.setCellRenderer(renderer);

        JScrollPane scrollTree = new JScrollPane(tree);
        scrollTree.setPreferredSize(new Dimension(300, 0));
        p.add(scrollTree, BorderLayout.WEST);

        //--- ÁREA DE CONTENIDO / TUTOR ---
        JPanel tutorPanel = new JPanel(new BorderLayout(10, 10));
        tutorPanel.setOpaque(false);

        JTextArea chat = new JTextArea();
        chat.setBackground(new Color(30, 41, 59));
        chat.setForeground(Color.WHITE);
        chat.setMargin(new Insets(15,15,15,15));
        chat.setEditable(false);
        chat.setText("Dax IA: Basado en tu perfil de Ingeniería (Noche), \nhemos priorizado el enfoque práctico.\n\nSelecciona una lección del índice para comenzar.");

        JTextField input = new JTextField();
        input.setBackground(new Color(51, 65, 85));
        input.setForeground(Color.WHITE);
        input.setCaretColor(Color.WHITE);
        input.setPreferredSize(new Dimension(0, 40));

        tutorPanel.add(new JScrollPane(chat), BorderLayout.CENTER);
        tutorPanel.add(input, BorderLayout.SOUTH);

        p.add(tutorPanel, BorderLayout.CENTER);

        return p;
    }

    // --- MÉTODOS DE APOYO ---
    private void agregarFila(JPanel p, String label, JComboBox<String> combo) {
        JLabel l = new JLabel(label);
        l.setForeground(TEXT_GRAY);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        p.add(l);
        p.add(combo);
    }

    private void validarCuestionario() {
        boolean completo = cbObj.getSelectedIndex() > 0 &&
                cbEnf.getSelectedIndex() > 0 &&
                cbGrado.getSelectedIndex() > 0 &&
                cbTiempo.getSelectedIndex() > 0 &&
                cbHora.getSelectedIndex() > 0;
        btnGenerar.setEnabled(completo);
    }

    private void estiloBoton(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DextechApp().setVisible(true));
    }
}
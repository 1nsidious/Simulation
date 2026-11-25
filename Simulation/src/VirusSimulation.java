	import javax.swing.*;
	import java.awt.*;
	
public class VirusSimulation extends JPanel{
	private final int CELL_SIZE = 36; //square size
	private final int GAP = 0;
	private VirusWorld world;
	private Timer timer;
//	private JButton startButton;
//	private JButton pauseButton;
	private JLabel statusLabel;
	
	public VirusSimulation() { //rows,columns,strength,health,lifetime,host health,host regeneration
		world = new VirusWorld(12,12,4,6,11,16,1);
		setBackground(new Color(16,25,28));
		setLayout(new BorderLayout(10,10));
		setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		JLabel title = new JLabel("Virus");
		title.setForeground(new Color(200,225,215));
		title.setFont(new Font("Arial", Font.BOLD,20));
		add(title,BorderLayout.NORTH);
		statusLabel = new JLabel();
		statusLabel.setForeground(new Color(235,245,240));
		statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
		timer = new Timer(350, e->{
			world.step();
			updateStatus();
			repaint();
			if(world.getVirusCount() == 0) {
				timer.stop();
				statusLabel.setText("The Virus has been defeated");
			}
			else if(world.allCellsDestroyed()) {
				timer.stop();
				statusLabel.setText("The Host has been infected");
			}
		});
		JButton startButton = new JButton("Start");
		startButton.addActionListener(e -> timer.start());
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(e -> timer.stop());
		
		JPanel controls = new JPanel();
		controls.setBackground(new Color(28,43,46));
		statusLabel.setOpaque(true);
		statusLabel.setBackground(new Color(28,43,46));
		controls.setForeground(Color.WHITE);
		controls.add(startButton);
		controls.add(pauseButton);
//		statusLabel = new JLabel();
		controls.add(statusLabel);
//		setLayout(new BorderLayout());
		add(controls, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(570, 600));
		updateStatus();
	}
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    int gridWidth = world.getColumns() * CELL_SIZE;
	    int startX = (getWidth() - gridWidth) / 2;
	    int startY = 65;

	    for (int row = 0; row < world.getRows(); row++) {
	        for (int column = 0;
	             column < world.getColumns(); column++) {
	            int x = startX + column * CELL_SIZE;
	            int y = startY + row * CELL_SIZE;
	            double health = world.getHostHealthAt(row, column);
	            if (health == 0) {
	                g2.setColor(new Color(24, 32, 35));
	            }
	            else if (health > world.getHostMaxHealth() * 0.66) {
	                g2.setColor(new Color(55, 125, 91));
	            }
	            else if (health > world.getHostMaxHealth() * 0.33) {
	                g2.setColor(new Color(126, 115, 65));
	            }
	            else {
	                g2.setColor(new Color(139, 67, 61));
	            }
	            g2.fillRect(x, y, CELL_SIZE + 1, CELL_SIZE + 1);
	            g2.setColor(new Color(20, 40, 37));
	            g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);

	            if (world.hasVirusAt(row, column)) {
	                int centerX = x + CELL_SIZE / 2;
	                int centerY = y + CELL_SIZE / 2;
	                g2.setColor(new Color(255, 105, 105));

	                for (int angle = 0; angle < 360; angle += 45) {
	                    double radians = Math.toRadians(angle);
	                    int x1 = centerX + (int)(7 * Math.cos(radians));
	                    int y1 = centerY + (int)(7 * Math.sin(radians));

	                    int x2 = centerX + (int)(13 * Math.cos(radians));
	                    int y2 = centerY + (int)(13 * Math.sin(radians));

	                    g2.drawLine(x1, y1, x2, y2);
	                }
	                g2.setColor(new Color(220, 55, 75));
	                g2.fillOval(centerX - 8,centerY - 8,16,16);
	                g2.setColor(Color.WHITE);
	                g2.fillOval(centerX - 3,centerY - 3,6,6);
	            }
	        }
	    }
	}
	private void updateStatus() { //update stats
		statusLabel.setText("Turn:" + world.getTurns() + " Viruses:" + world.getVirusCount() + " Cells destroyed:" + world.getDestroyedCount() + " Immunity:" + world.getImmunity());
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->{
			JFrame frame = new JFrame("Virus"); //creates window
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //quit
			frame.setContentPane(new VirusSimulation()); //add simulation to window
			frame.pack(); //window size
			frame.setLocationRelativeTo(null); //center window
			frame.setVisible(true); // shows window
		});
	}
}
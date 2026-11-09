import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Window extends JFrame{
	private Simulation world;
	private Timer simulationTimer;
	private SimulationPanel drawPanel;
	private JButton startButton;
	private JButton stopButton;
	private JLabel dnaCounterLabel;
		
	public Window() {
		setTitle("Simulation");
		setSize(600,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		world = new Simulation(20,20,2);
		world.addAgent(new Agent(5,5));
		world.addAgent(new Agent(10,10));
		
		drawPanel = new SimulationPanel(world);
		add(drawPanel, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		dnaCounterLabel = new JLabel("DNA Research");
		
		controlPanel.add(startButton);
		controlPanel.add(stopButton);
		controlPanel.add(dnaCounterLabel);
		add(controlPanel, BorderLayout.SOUTH);
		
		simulationTimer = new Timer(300, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				world.step();
				int totalDna = 0;
				for(Agent a : world.getAgents()) {
					totalDna += a.getCollectedDna();
				}
				dnaCounterLabel.setText("Total DNA Collected: " + totalDna);
				drawPanel.repaint();
			}
		});
		startButton.addActionListener(e -> simulationTimer.start());
		stopButton.addActionListener(e -> simulationTimer.stop());
	}
	private class SimulationPanel extends JPanel{
		private Simulation simulationWorld;
		public SimulationPanel(Simulation world) {
			this.simulationWorld = world;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Tile[][] grid = simulationWorld.getGrid();
		int width = grid.length;
		int height = grid[0].length;
		
		int cellWidth = getWidth() / width;
		int cellHeight = getHeight() / height;
			
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				Tile tile = grid[x][y];
				int dna = tile.getDnaAmount();
					
				if(dna > 5) {
					g.setColor(new Color(40,160,40));
				}
				else if (dna > 0) {
					g.setColor(new Color(180,180,40));
				}
				else {
					g.setColor(new Color(80,80,80));
				}
				g.fillRect(x * cellWidth, y * cellHeight, cellWidth -1, cellHeight -1);
			}
		}
		for(Agent agent : simulationWorld.getAgents()) {
			g.setColor(Color.RED);
			g.fillOval(agent.getX() * cellWidth, agent.getY() * cellHeight, cellWidth, cellHeight);	
			}
		}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->{
			new Window().setVisible(true);
		});
	}
}
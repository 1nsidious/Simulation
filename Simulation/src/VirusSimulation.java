	import javax.swing.*;
	import java.awt.*;
public class VirusSimulation extends JPanel{
	private final int CELL_SIZE = 38; //square size
	private VirusWorld world;
	private Timer timer;
	private JButton startButton;
	private JButton pauseButton;
	private JLabel statusLabel;
	
	public VirusSimulation() {
		world = new VirusWorld(12,12,2,6,1);
		timer = new Timer(400, e->{
			world.step();
			updateStatus();
			repaint();
			if(world.getInfectedCount() == world.getRows() * world.getColumns()) {
				timer.stop();
				updateStatus();
			}
		});
		startButton = new JButton("Start");
		startButton.addActionListener(e -> timer.start());
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(e -> timer.stop());
		
		JPanel controls = new JPanel();
		controls.add(startButton);
		controls.add(pauseButton);
		statusLabel = new JLabel();
		controls.add(statusLabel);
		setLayout(new BorderLayout());
		add(controls, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(520, 520));
		updateStatus();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int row = 0; row < world.getRows(); row++) {
			for(int column = 0; column < world.getColumns(); column++) {
				if(world.hasVirusAt(row, column)) {
					g.setColor(Color.RED);
				}
				else {
					g.setColor(Color.GREEN);
				}
				int x = 20 + column * CELL_SIZE; //make squares
				int y = 20 + row * CELL_SIZE;
				g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
				g.setColor(Color.BLACK); //black border around squares
				g.drawRect(x , y, CELL_SIZE, CELL_SIZE);
			}
		}
	}
	private void updateStatus() { //update stats
		statusLabel.setText("Turn:" + world.getTurns() + " | Infected:" + world.getInfectedCount() + "/" + (world.getRows() * world.getColumns()));
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
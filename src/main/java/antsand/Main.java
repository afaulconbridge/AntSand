package antsand;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		new Main().run();
	}

	private void run() {
		
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setTitle("Ant Sand");		
		
		Sim sim = new Sim(640,480);
		SandPanel panel = new SandPanel(sim);
		frame.add(panel);
		
		frame.setVisible(true);
	}
}

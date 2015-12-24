package antsand;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntSandMain {
	
	protected Sim sim;
	protected JFrame frame;
	protected boolean simRunning = true;
	protected SandPanel sandPanel = null;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		new AntSandMain().run();
	}

	private void run() {		
		frame = new JFrame();
		frame.setSize(640, 480);
		frame.setTitle("Ant Sand");		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		sim = new Sim(640,480);
		sandPanel = new SandPanel(sim);
		frame.add(sandPanel, BorderLayout.CENTER);
		
		
		//put a toolbar over the top		
		JToolBar toolBar = new JToolBar();
		frame.add(toolBar, BorderLayout.NORTH);
		
		//add some buttons to the toolbar
		JButton buttonPlay = new JButton(">");
		buttonPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simRunning = true;
			}
		});
		toolBar.add(buttonPlay);
		
		JButton buttonPause = new JButton("||");
		buttonPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simRunning = false;
			}
		});
		toolBar.add(buttonPause);
		
		String[] penSizeStrings = {"Pen 1","Pen 5","Pen 25"};
		JComboBox<String> penSizeComboBox = new JComboBox<>(penSizeStrings);
		penSizeComboBox.setSelectedIndex(0);
		penSizeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        @SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String pen = (String)cb.getSelectedItem();
		        int size = Integer.parseInt(pen.substring(4));
		        sandPanel.setPenSize(size);
		    }
		});
		toolBar.add(penSizeComboBox);
		
		String[] penTypeStrings = {"Sand", "Air"};
		JComboBox<String> penTypeComboBox = new JComboBox<>(penTypeStrings);
		penTypeComboBox.setSelectedIndex(0);
		penTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        @SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String pen = (String)cb.getSelectedItem();
		        if (pen.equals("Sand")) {
		        	sandPanel.setPenMaterial(SiteType.SAND);
		        } else if (pen.equals("Air")) {
		        	sandPanel.setPenMaterial(SiteType.AIR);
		        }
		    }
		});
		toolBar.add(penTypeComboBox);
		
		frame.setVisible(true);
		
		simThread.start();
		
		log.info("Started");
	}
	
	protected Thread simThread = new Thread() {

		@Override
		public void run() {
			log.info("running...");
			while (true) {
				if (simRunning) {
					log.info("updating...");
					
					long timeStart = System.nanoTime();
					sim.update();
					long timeUpdate = System.nanoTime();
					frame.repaint();
					long timeFrame = System.nanoTime();
					log.info("Timing: sim "+(timeUpdate-timeStart)/1000+" : frame "+(timeFrame-timeUpdate)/1000);
				}
				try {
					sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
}

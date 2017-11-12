package eukaryote.iotawallet.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;

import eukaryote.iotawallet.ApiWrapper;
import eukaryote.iotawallet.AppContext;

import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ApplicationWindow {

	private JFrame frame;
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JMenuBar menuBar;
	private JMenu filemenu;
	private JMenu walletmenu;
	private JMenu toolsmenu;
	private JMenu helpmenu;
	private JMenuItem fileopenitem;
	private JMenuItem helpaboutitem;
	private AppContext ctx;
	private HistoryPanel historypanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ApplicationWindow window = new ApplicationWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		ctx = new AppContext();
		initialize();
		
		for (HistoryEntry e : ctx.getApi().getHistory())
			historypanel.getModel().addEntry(e);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		historypanel = new HistoryPanel();
		tabbedPane.addTab("History", null, historypanel, null);
		tabbedPane.addTab("Send", null, new JPanel(), null);
		tabbedPane.addTab("Recieve", null, new JPanel(), null);
		
		menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		filemenu = new JMenu("File");
		filemenu.setMnemonic('F');
		menuBar.add(filemenu);
		
		walletmenu = new JMenu("Wallet");
		walletmenu.setMnemonic('W');
		menuBar.add(walletmenu);
		
		toolsmenu = new JMenu("Tools");
		toolsmenu.setMnemonic('T');
		menuBar.add(toolsmenu);
		
		helpmenu = new JMenu("Help");
		helpmenu.setMnemonic('H');
		menuBar.add(helpmenu);
		
		helpaboutitem = new JMenuItem("About");
		helpaboutitem.setMnemonic('A');
		helpmenu.add(helpaboutitem);
	}

}

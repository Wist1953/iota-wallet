package eukaryote.iotawallet.gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

public class SendPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public SendPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("50dlu"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblHp = new JLabel("HP");
		add(lblHp, "1, 2");

	}

}

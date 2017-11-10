package eukaryote.iotawallet.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class HistoryPanel extends JPanel {
	private JTable table;
	private HistoryTableModel model;

	/**
	 * Create the panel.
	 */
	public HistoryPanel() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		add(scrollPane);

		this.model = new HistoryTableModel();
		table = new JTable(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					JTable table = (JTable) mouseEvent.getSource();
					Point point = mouseEvent.getPoint();
					int row = table.rowAtPoint(point);
					handleTransactionClicked(model.getHistory().get(row));
				}
			}
		});
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);

		for (int i = 0; i < 100; i++)
			this.getModel().addEntry(new HistoryEntry(i, "tag" + i, i + 1000, i + 2000));
	}

	/**
	 * @return the model
	 */
	public HistoryTableModel getModel() {
		return model;
	}
	
	private void handleTransactionClicked(HistoryEntry e) {
		// TODO: show popup
		System.out.println(e);
	}

	@SuppressWarnings("serial")
	public class HistoryTableModel extends AbstractTableModel {
		private String[] columns = { "Date", "Tag", "Amount", "Balance" };

		private List<HistoryEntry> history = new ArrayList<>();

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			return getHistory().size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return history.get(rowIndex).getObjectAt(columnIndex);
		}

		public void addEntry(HistoryEntry entry) {
			this.history.add(0, entry);
			this.fireTableDataChanged();
		}

		@Override
		public String getColumnName(int col) {
			return columns[col].toString();
		}

		/**
		 * @return the history
		 */
		public List<HistoryEntry> getHistory() {
			return Collections.unmodifiableList(history);
		}

		/**
		 * @param history
		 *            the history to set
		 */
		public void setHistory(List<HistoryEntry> history) {
			this.history = history;
			this.fireTableDataChanged();
		}

	}

}

package com.opensme.frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.opensme.backend.models.Portfolio;
import com.opensme.backend.models.PortfolioItem;
import com.opensme.backend.handlers.PortfolioHandler;

public class PortfolioUI extends JFrame {
    private JList<Portfolio> portfolioList;
    private DefaultListModel<Portfolio> listModel;
    private JTable itemTable;
    private DefaultTableModel tableModel;
    private PortfolioHandler portfolioHandler;

    public PortfolioUI() {
        setTitle("Portfolio Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        portfolioHandler = new PortfolioHandler();
        listModel = new DefaultListModel<>();
        portfolioList = new JList<>(listModel);
        portfolioList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(portfolioList);

        tableModel = new DefaultTableModel();
        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(itemTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, tableScrollPane);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        List<Portfolio> portfolios = portfolioHandler.getPortfolios();
        for (Portfolio portfolio : portfolios) {
            listModel.addElement(portfolio);
        }

        portfolioList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Portfolio selected = portfolioList.getSelectedValue();
                if (selected != null) {
                    populateTable(portfolioHandler.getItems(selected.getId()));
                }
            }
        });

        portfolioList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Portfolio) {
                    setText(((Portfolio)value).getName());
                }
                return this;
            }
        });
    }

    private void populateTable(List<PortfolioItem> items) {
        String[] columns = {"Symbol", "Shares", "Buy Price", "Currency"};
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);
        
        if (items.isEmpty()) {
            return;
        }
        
        for (PortfolioItem item : items) {
            tableModel.addRow(new Object[]{
                item.getSymbol(),
                item.getShares(),
                item.getBuyPrice(),
                item.getCurrency()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PortfolioUI().setVisible(true);
        });
    }
}

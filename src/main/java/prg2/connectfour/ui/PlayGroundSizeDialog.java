package prg2.connectfour.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import prg2.connectfour.utils.Pair;

public class PlayGroundSizeDialog {

    public static Pair<Integer, Integer> showDialog() {

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setGroupingUsed(false);

        JTextField xField = new JFormattedTextField(decimalFormat);
        JTextField yField = new JFormattedTextField(decimalFormat);
        
        // Default size
        xField.setText("7");
        yField.setText("6");

        xField.setColumns(3);
        yField.setColumns(3);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("x:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("y:"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Pair<Integer, Integer> size = new Pair<>(Integer.valueOf(xField.getText()), Integer.valueOf(yField.getText()));
            return size;
        }
        return null;
    }

}

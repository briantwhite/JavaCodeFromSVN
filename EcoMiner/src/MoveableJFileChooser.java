import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class MoveableJFileChooser extends JFileChooser {
	protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
		Dimension screenSize = this.getToolkit().getScreenSize();
		dialog.setLocation((screenSize.width / 3), (screenSize.height / 3));
        return dialog;
    }
}

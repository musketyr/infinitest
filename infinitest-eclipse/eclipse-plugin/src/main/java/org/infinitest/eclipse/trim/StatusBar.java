package org.infinitest.eclipse.trim;

import static org.eclipse.swt.SWT.*;
import static org.infinitest.eclipse.util.GraphicsUtils.*;

import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.menus.AbstractWorkbenchTrimWidget;
import org.infinitest.eclipse.InfinitestPlugin;
import org.infinitest.eclipse.PluginActivationController;

public class StatusBar extends AbstractWorkbenchTrimWidget implements VisualStatus
{
    /**
     * Cache the current trim so we can 'dispose' it on demand
     */
    private Composite composite;
    private Label statusLabel;
    private String statusString = "Infinitest is waiting for changes";
    private int backgroundColor = COLOR_BLACK;

    @Override
    public void init(IWorkbenchWindow workbenchWindow)
    {
        getPluginController().attachVisualStatus(this);
    }

    @Override
    public void dispose()
    {
        if (composite != null && !composite.isDisposed())
        {
            composite.dispose();
        }
        composite = null;
    }

    @Override
    public void fill(Composite parent, int oldSide, int newSide)
    {
        composite = new Composite(parent, NONE);

        RowLayout layout = new RowLayout();
        layout.marginHeight = 4;
        layout.marginWidth = 2;
        layout.center = true;
        composite.setLayout(layout);

        addStatusLabel(newSide);
        composite.layout();
    }

    private void addStatusLabel(int newSide)
    {
        statusLabel = new Label(composite, BORDER | LEFT);
        statusLabel.setLayoutData(getRowData(newSide));
        statusLabel.setForeground(getColor(COLOR_WHITE));
        statusLabel.setText(statusString);
        statusLabel.setBackground(getColor(backgroundColor));
    }

    RowData getRowData(int newSide)
    {
        RowData rowData = new RowData();
        if (newSide == BOTTOM || newSide == TOP)
        {
            rowData.width = 400;
        }
        return rowData;
    }

    public void setBackgroundColor(final int systemColor)
    {
        this.backgroundColor = systemColor;
        Display.getDefault().asyncExec(new Runnable()
        {
            public void run()
            {
                statusLabel.setBackground(getColor(systemColor));
                composite.redraw();
            }
        });
    }

    public void setText(final String statusString)
    {
        this.statusString = statusString;
        Display.getDefault().asyncExec(new Runnable()
        {
            public void run()
            {
                if (!statusLabel.getText().equals(statusString))
                {
                    statusLabel.setText(statusString);
                    composite.redraw();
                }
            }
        });
    }

    public void setToolTip(final String tooltip)
    {
        Display.getDefault().asyncExec(new Runnable()
        {
            public void run()
            {
                statusLabel.setToolTipText(tooltip);
            }
        });
    }

    protected PluginActivationController getPluginController()
    {
        return InfinitestPlugin.getInstance().getPluginController();
    }

    public void setTextColor(final int color)
    {
        Display.getDefault().asyncExec(new Runnable()
        {
            public void run()
            {
                statusLabel.setForeground(getColor(color));
            }
        });
    }
}
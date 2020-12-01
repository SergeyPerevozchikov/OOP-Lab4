
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer extends JFrame
{
	private int size;
	private JImageDisplay imageDisp;
	private FractalGenerator generator;
	private Rectangle2D.Double range;
	
	public FractalExplorer(int size)
	{
		this.size = size;
		this.range = new Rectangle2D.Double();
		this.generator = new Mandelbrot();
		this.generator.getInitialRange(range);
	}
	
	public void createAndShowGUI()
	{
		JFrame frame = new JFrame("Fractal");
        JButton button = new JButton("Reset");
		imageDisp = new JImageDisplay(size,size);
		
		ResetEvent resetEvent = new ResetEvent();
        MouseHandler mouseHandler = new MouseHandler();
		imageDisp.setLayout(new BorderLayout());
		
		imageDisp.addMouseListener(mouseHandler);
        button.addActionListener(resetEvent);
		
		frame.add(imageDisp,BorderLayout.CENTER);
        frame.add(button,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
	}
	
	private void drawFractal()
	{
		for(int x = 0; x < size; x++)
		{
            for(int y = 0; y < size; y++)
			{
				double xCoord  =  FractalGenerator.getCoord(range.x,  range.x  +  range.width, size, x);
				double yCoord  =  FractalGenerator.getCoord(range.y,  range.y  +  range.height, size, y);
				int iteration = generator.numIterations(xCoord,yCoord);
				if (iteration == -1)
				{
					imageDisp.drawPixel(x,y,0);
				}
				else
				{
					float hue = 0.7f + (float) iteration / 200f; 
					int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
					imageDisp.drawPixel(x,y,rgbColor);
				}
			}
		}
		imageDisp.repaint();
	}
	
	private class ResetEvent implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            generator.getInitialRange(range);
            drawFractal();
        }
    }
	
	private class MouseHandler extends MouseAdapter{
        
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);
            generator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
	
	public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
	
}
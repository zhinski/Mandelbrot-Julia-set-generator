import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.function.IntFunction;
import javax.swing.JPanel;


public class ComplexPlaneGraphicsPanel extends JPanel {
	private static final long serialVersionUID = 6950486031136411438L;
	
	private ComplexNumber min;
	private ComplexNumber max;
	protected boolean isMandel = true;
	private MainFrame mf;
	
	private IntFunction<Color> stepToColorFunction() {
		return new IntFunction<Color>() {
//			@Override
//			public Color apply(int value) {
//				return new Color(	(int)((double)255*(1.0-Math.cbrt((double)value/(double)Mandelbrot.MAX_DEPTH))),
//									(int)((double)0*(1.0-Math.cbrt((double)value/(double)Mandelbrot.MAX_DEPTH))),
//									(int)((double)0*(1.0-Math.cbrt((double)value/(double)Mandelbrot.MAX_DEPTH)))		);
//			}
			@Override
			public Color apply(int value) {
				return mf.interpol.iterToColor(value);
			}
		};
	}
	
	public ComplexPlaneGraphicsPanel(MainFrame mf) {
		super();
		this.mf = mf;
		
		min = new ComplexNumber(-2.0, -2.0);
		max = new ComplexNumber(2.0, 2.0);
	}
	

	@Override
	public void paint(Graphics g) {

		super.paint(g);
		paintBackground(g);
	}
	
	private void paintBackground(Graphics g) {
		
		double rStep = (max.getReal() - min.getReal()) / getWidth();
		double iStep = (min.getImaginary() - max.getImaginary()) / getHeight();
		
		BufferedImage imgNew = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		mf.interpol.generatePoly(mf.menu.colorBar.getColorScheme());////////////////////////////////////////////////////////////////////////
		if (isMandel) {
			for(int y = 0; y < getHeight(); y++) {
				
				for(int x =0; x < getWidth(); x ++) {
					
					imgNew.setRGB(
						x,
						y,
						stepToColorFunction().apply(
							Mandelbrot.altApply(
								new ComplexNumber(
									min.getReal() + x*rStep,
									max.getImaginary() + y*iStep
									)
								)
							).getRGB()
						);
				}
			}
		}
		else {
			for(int y = 0; y < getHeight(); y++) {
				
				for(int x =0; x < getWidth(); x ++) {
					
					imgNew.setRGB(
						x,
						y,
						stepToColorFunction().apply(
							mf.juliaRule.findDepth(
								new ComplexNumber(
									min.getReal() + x*rStep,
									max.getImaginary() + y*iStep
									)
								)
							).getRGB()
						);
				}
			}
		}
		g.drawImage(imgNew, 0, 0, null);
	}
	
	@Override
	public Dimension getPreferredSize() {

		return getParent().getSize();

	}
	
	public ComplexNumber getMin() {
		return this.min;
	}
	
	public ComplexNumber getMax() {
		return this.max;
	}
	public void setMin(ComplexNumber cnum) {
		this.min = cnum;
	}
	
	public void setMax(ComplexNumber cnum) {
		this.max = cnum;
	}
	
	private class iterationsToColorFunction implements IntFunction<Color> {

		Color[] colors;
		
		public iterationsToColorFunction(Color...colors) {
			this.colors = colors;
		}
		
		public void update(Color...colors) {
			this.colors = colors;
		}
		
		@Override
		public Color apply(int value) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}

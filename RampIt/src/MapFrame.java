import gis.Point;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MapFrame extends JFrame implements ActionListener,MouseListener,MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapPanel mapView;
	private JButton zoomIn;
	private JButton zoomOut;
	private Point sDrag;
	public MapFrame(ArrayList<Segment>map){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapView = new MapPanel();
		for(Segment s:map){
			mapView.addSegment(s);
		}
		//this.setLayout(BorderLayout);
		this.add(mapView,BorderLayout.CENTER);
		JPanel tools = new JPanel();
		zoomIn = new JButton("+");
		zoomIn.addActionListener(this);
		zoomOut = new JButton("-");
		zoomOut.addActionListener(this);
		tools.setLayout(new FlowLayout());
		tools.add(zoomIn);
		tools.add(zoomOut);
		add(tools,BorderLayout.NORTH);
		mapView.addMouseMotionListener(this);
		mapView.addMouseListener(this);
		this.setMinimumSize(new Dimension(600,600));
		mapView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sDrag = new Point(0,0);
		pack();
		setVisible(true);
	}
	public void actionPerformed(ActionEvent evt){
		Object src = evt.getSource();
		if(src == zoomIn){
			mapView.zoomIn();
		}else if(src == zoomOut){
			mapView.zoomOut();
		}
		repaint();
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		int xOffset = (int) (arg0.getX() - sDrag.getX());
		int yOffset = (int) (arg0.getY() - sDrag.getY());
		sDrag = new Point(arg0.getX(),arg0.getY());
		mapView.shift(xOffset,yOffset);
		repaint();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.print(e.getX());
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		sDrag = new Point(e.getX(),e.getY());
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

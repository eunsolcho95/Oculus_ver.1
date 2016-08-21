import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * 메인창
 * @author Sol
 * @param public static int Year - 연도 지정
 * @param public static int Month - 월 지정
 * @param public static int Day - 일 지정
 * @param public static int FM - Follow Me 목표 횟수
 * @param public static int FMC - Follow Me 카운트
 * @param public static int FD - Fifteen Dots 목표 횟수
 * @param public static int FDC - Fifteen Dots 카운트
 *
 */
class MainFrame extends JFrame implements WindowListener {
	public static int Year = -1;		
	public static int Month = -1;
	public static int Day = -1;
	public static int FM = -1; 
	public static int FMC = -1; 
	public static int FD = -1; 
	public static int FDC = -1; 

	private JLabel date;
	private JButton record; 
	private JButton graph; 
	private JButton goal;
	private JButton exercise1;
	private JButton exercise2; 
	private JButton exercise3; 
	private JButton reference; 

	private Calendar cal;

	/*창이 포커스를 받을 때마다 기록을 불러오고 하루가 바꼈다면 기록 저장*/
	public void windowActivated(WindowEvent e) {
		Save.Load();										
		Save.SaveDay();			
	}
	
	public void windowClosed(WindowEvent e) {
	}

	/*창을 닫으면 트레이를 시작하고 시계 쓰레드를 종료한다. 트레이로 돌려보냈을 시에 지정시간(50분)이 되면 운동 중 하나가 자동으로 실행하게 함*/
	public void windowClosing(WindowEvent e) {
		new Tray(); 
		DateJLabel.stop(); 
		Thread Waiting = new Thread(new Waiting()); 
		Waiting.start();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	/*창을 내렸을 경우 지정시간(50분)이 되면 운동 중 하나가 자동으로 실행하게 함*/
	public void windowIconified(WindowEvent e) {
		Thread Waiting = new Thread(new Waiting()); 
		Waiting.start();
	}

	/*창을 올리면 Waiting 종료*/
	public void windowDeiconified(WindowEvent e) {
		Waiting.finish(); 
	}

	/*트레이에서 돌아오면 Waiting 종료*/
	public void windowOpened(WindowEvent e) {
		Waiting.finish(); 
	}

	/*창을 가운데에 띄우고 시계, 기록, 운동들, 레퍼런스 버튼 달기*/
	MainFrame() {
		setTitle("Oclulus");
		setSize(750, 1000);
		setLocationRelativeTo(null);
		setVisible(true);

		this.addWindowListener(this);
		cal = Calendar.getInstance(); 
		
		Save.Load();			

		date = new JLabel("현재 날짜시각", null, SwingConstants.CENTER);

		record = new JButton("월간 기록 확인");
		record.setFont(new Font("Gothic", Font.BOLD, 20));
		record.setBackground(new Color(93, 93, 93));
		record.setForeground(Color.white);
		record.setUI(new StyledButtonUI()); 
		
		graph = new JButton("오늘 목표달성율(%)");
		graph.setFont(new Font("Gothic", Font.BOLD, 20));
		graph.setBackground(new Color(93, 93, 93));
		graph.setForeground(Color.white);
		graph.setUI(new StyledButtonUI()); 
		
		goal = new JButton("설정");
		goal.setFont(new Font("Gothic", Font.BOLD, 20));
		goal.setBackground(new Color(93, 93, 93));
		goal.setForeground(Color.white);
		goal.setUI(new StyledButtonUI());
		
		exercise1 = new JButton("Follow Me");
		exercise1.setFont(new Font("Arial", Font.BOLD, 20));
		exercise1.setBackground(new Color(93, 93, 93));
		exercise1.setForeground(Color.white);
		exercise1.setUI(new StyledButtonUI());
		
		exercise2 = new JButton("Fifteen Dots");
		exercise2.setFont(new Font("Gothic", Font.BOLD, 20));
		exercise2.setBackground(new Color(93, 93, 93));
		exercise2.setForeground(Color.white);
		exercise2.setUI(new StyledButtonUI()); 
		
		exercise3 = new JButton("Brightness");
		exercise3.setFont(new Font("Gothic", Font.BOLD, 20));
		exercise3.setBackground(new Color(93, 93, 93));
		exercise3.setForeground(Color.white);
		exercise3.setUI(new StyledButtonUI()); 
		
		reference = new JButton("가천대학교 의용생체공학과 소개");
		reference.setFont(new Font("Gothic", Font.BOLD, 20));
		reference.setBackground(new Color(93, 93, 93));
		reference.setForeground(Color.white);
		reference.setUI(new StyledButtonUI()); 
		
		Container mContainer = getContentPane();
		mContainer.setLayout(new GridLayout(4, 2, 30, 30)); 
		
		mContainer.add(date);
		mContainer.add(record);
		mContainer.add(graph);
		mContainer.add(goal);
		mContainer.add(exercise1);
		mContainer.add(exercise2);
		mContainer.add(exercise3);
		mContainer.add(reference);
	}


	public void runRecord() {
		record.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Record record = new Record();
				record.run();
			}
		});
	}

	public void runGraph() {
		graph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph graph = new Graph();
			}
		});
	}

	public void runGoal() {
		goal.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Thread goal = new Thread(new Goal());
				goal.start();
			}
		});
	}

	public void runExercise1() {
		exercise1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread followMe = new Thread(new FollowMe());
				followMe.start();
			}
		});
	}

	public void runExercise2() {
		exercise2.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Thread dots = new Thread(new Dots());
				dots.start();
			}
		});
	}

	public void runExercise3() {
		exercise3.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Thread brightness = new Thread(new Brightness());
				brightness.start();
			}
		});
	}

	public void runReference() {
		reference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reference reference = new Reference();
			}
		});
	}

	public void setDate(JLabel date) {
		this.date = date;
	}

	public void setRecord() {
		return;
	}

	public void setGraph() {
		return;
	}

	public void setGoal() {
		return;
	}

	public void setExercise1() {
		return;
	}

	public void setExercise2() {
		return;
	}

	public void setExercise3() {
		return;
	}

	public void setReference() {
		return;
	}

	public JLabel getDate() {
		return date;
	}

	public JButton getRecord() {
		return record;
	}

	public JButton getGraph() {
		return graph;
	}

	public JButton getGoal() {
		return goal;
	}

	public JButton getExercise1() {
		return exercise1;
	}

	public JButton getExercise2() {
		return exercise2;
	}

	public JButton getExercise3() {
		return exercise3;
	}

	public JButton getReference() {
		return reference;
	}
}

/*시계, 운동 쓰레드 시작*/
public class Main {
	public static void main(String[] args) {
		MainFrame mFrame = new MainFrame();

		Calendar now = Calendar.getInstance(); 

		Thread dateJLabel = new Thread(new DateJLabel(now, mFrame.getDate())); 
		
		dateJLabel.start();

		mFrame.runRecord();
		mFrame.runGraph();
		mFrame.runGoal();
		mFrame.runExercise1();
		mFrame.runExercise2();
		mFrame.runExercise3();
		mFrame.runReference();
	}


	/**
	 * 버튼 UI 설정 클래스
	 * @author Sol
	 *
	 */
	class StyledButtonUI extends BasicButtonUI {

		@Override
		public void installUI(JComponent c) {
			super.installUI(c);
			AbstractButton button = (AbstractButton) c;
			button.setOpaque(false);
			button.setBorder(new EmptyBorder(5, 15, 5, 15));
		}

		@Override
		public void paint(Graphics g, JComponent c) {
			AbstractButton b = (AbstractButton) c;
			paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
			super.paint(g, c);
		}

		private void paintBackground(Graphics g, JComponent c, int yOffset) {
			Dimension size = c.getSize();
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(c.getBackground().darker());
			g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
			g.setColor(c.getBackground());
			g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
		}
	}
}

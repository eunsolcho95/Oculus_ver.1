// 05. 안구이동근 운동 : 움직이는 점 트레이닝

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class FollowMe extends JFrame implements Runnable, WindowListener{
	public static int spset = 3;		//공 스피드 디폴트 값(랜덤)
	public static int szset = 0;		//공 크기 디폴트 값(중)
	Setting setting;					//설정창
	Container contentPane;
	boolean flag;	
	JButton btn; 						//공 설정 버튼
	HowToUse htu1;						//설명서 설정
	
	Color color = new Color(206,247,110);							//백그라운드 색깔 설정
	Dimension res = Toolkit.getDefaultToolkit().getScreenSize(); 	//전체화면 사이즈 가져오기		//http://b-jay.tistory.com/123

	String [] sptext = {"하", "중", "상", "랜덤"};
	String [] sztext = {"중", "소"};
	
	int speed1[]= {res.width/350, res.width/250, res.width/150};			 					//창크기에 따른 공 스피드(하, 중, 상)
	int speed2[] = {res.width/350, res.width/250, res.width/200, res.width/50};					//창크기에 따른 랜덤 공 스피드
	int size[] = {res.width/15, res.width/20};													//컴퓨터 창 크기 따른 상대적인 공 크기(중, 하)

	//인터페이스를 상속했으므로 다 오버로딩해야함
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		finish();				//닫았을 경우 count 안하게 함
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	FollowMe() {
		super("FolloFollow Me");
		contentPane = getContentPane();
		contentPane.setLayout(null); 								//배치관리자 삭제
		contentPane.setBackground(color);
		DrawCircle101 panel = new DrawCircle101();					// 패널을 새로 생성
		panel.setSize(res.width-100,res.height); 					//범위 지정
		panel.setBackground(color);
		contentPane.add(panel); 									// 패널을 컨텐트팬에 부착
		this.addWindowListener(this);

		flag = false;		

		btn = new JButton("메뉴"); 									//버튼 만들기

		btn.setSize(50,50);											//버튼 사이즈 지정
		btn.setLocation(res.width-70,30);							//버튼 위치 지정
		contentPane.add(btn);

		//마우스 리스너 추가
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				setting.setVisible(true);
			}
		});

		setting = new Setting(this, "설정");
		setting.setLocationRelativeTo(null);

		setSize(res.width, res.height);
		setVisible(true);

		htu1 = new HowToUse(1);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	void finish(){
		flag = true;
	}

	public void run(){
		int k = -10;
		while(true){
			try{
				//Thread.sleep(100);   // 디버깅용
				Thread.sleep(1000);   // 60초 후 종료

				if(flag == true){
					htu1.finish();
					dispose();
					return;
				}
			}catch(InterruptedException e){
				System.err.println(e);
				return;
			}

			k++;
			if(k == -5)
				htu1.finish();			//5초가 지나면 설명문 사라지게 하기

			if(k == 60){
				MainFrame.FMC++;
				if(MainFrame.FMC > MainFrame.FM) MainFrame.FMC = MainFrame.FM;
				Save.SaveNow();
				
				dispose();
				return;
			}
		}
	}

	public class DrawCircle101 extends JPanel {
		private int xPos=200; //x 좌표
		private int yPos=100; //y 좌표

		int speedX; 	//x좌표로의 스피드
		int speedY; 	//y좌표로의 스피드
		int speedx;		//다음 스피드 저장
		int speedy;

		ImageIcon icon = new ImageIcon("image\\RYAN.png");
		Image img = icon.getImage();

		boolean play = true;

		public DrawCircle101() {
			speedX = speedY = 10;
			try {
				new Thread(){
					public void run(){
						while(play){
							Random rd = new Random();
							//Random.nextInt():int형의 난수를 발생
							//Random.nextInt(int n) : 0과 (n-1)사이의 int형의 난수를 발생한다.

							int x_dir = rd.nextInt(2);  //0~1 난수 발생 (스피드를 음수로 할지 양수로 할지 랜덤으로 하려는듯)
							int y_dir = rd.nextInt(2);

							int x_rnd = rd.nextInt(4);
							int y_rnd = rd.nextInt(4);
							
							if(spset <3){							//하, 중, 상을 골랐을 경우
								speedx= speed1[spset];
								speedy= speed1[spset];
							}

							if(spset ==3){							//랜덤을 골랐을 경우
								speedx = speed2[x_rnd];
								speedy = speed2[y_rnd];
							}

							xPos = xPos + speedX; //x좌표 이동
							yPos = yPos + speedY; //y좌표 이동

							if(xPos + size[szset] >=DrawCircle101.this.getWidth() ){
								//창의 끝에 닿았으므로 speedx를 반대로
								speedX = -speedx;
								speedY = (y_dir == 0 ? speedy : -speedy);
							}

							if( yPos+ size[szset] >=DrawCircle101.this.getHeight()){
								speedY = -speedy;
								speedX = (x_dir == 0 ? speedx : -speedx);
							}

							if(xPos < 0){
								speedX = speedx;
								speedY = (y_dir == 0 ? speedy : -speedy);
							}

							if(yPos < 0){
								speedY = speedy;
								speedX = (x_dir == 0 ? speedx : -speedx);
							}

							repaint(); //공이 움직이기때문에 다시 그리기
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;

			g.drawString("공 크기 : "+sztext[szset], res.width-170, 50);
			g.drawString("공 속도 : "+sptext[spset], res.width-170, 70);

			g2d.drawImage(img, xPos, yPos, size[szset], size[szset], this);			//x, y 위치에 size 만큼의 이미지 그리기
		}
	}
}


//예제 9-5, 11-5, 11-6 활용
class Setting extends JDialog {
	Container cp;

	int sp=3;
	int sz=0;

	JLabel csize = new JLabel("공 크기");
	JLabel cspeed = new JLabel("공 속도");

	JRadioButton [] speed = new JRadioButton[4];
	JRadioButton [] size = new JRadioButton[2];

	String [] sptext = {"하", "중", "상", "랜덤"};
	String [] sztext = {"중", "소"};

	public Setting(JFrame frame, String title) {
		super(frame, title);

		cp = getContentPane();
		cp.setLayout(null);

		//속도 선택 그룹
		ButtonGroup gspeed = new ButtonGroup();
		for(int i=0; i<speed.length; i++){
			speed[i] = new JRadioButton(sptext[i]);
			gspeed.add(speed[i]);
			speed[i].addItemListener(new SpListener()); //예제 11-6
		}
		speed[sp].setSelected(true); //초기조건 : 랜덤

		//공 크기 선택 그룹
		ButtonGroup gsize = new ButtonGroup();
		for(int i=0; i<size.length; i++){
			size[i] = new JRadioButton(sztext[i]);
			gsize.add(size[i]);
			size[i].addItemListener(new SzListener());
		}
		size[sz].setSelected(true); //초기조건 : 중


		//확인 취소 버튼
		JButton ok = new JButton("확인");
		ok.setBackground(new Color(93,93,93));
		ok.setForeground(Color.white);
		// customize the button with your own look
		ok.setUI(new StyledButtonUI());
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FollowMe.spset = sp;			//공 스피드, 공사이즈 설정
				FollowMe.szset = sz;
				setVisible(false);				
			}
		});

		JButton cancel = new JButton("취소");
		cancel.setBackground(new Color(93,93,93));
		cancel.setForeground(Color.white);
		// customize the button with your own look
		cancel.setUI(new StyledButtonUI());
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);				
			}
		});


		//공 크기 위치지정
		csize.setLocation(135,20);
		csize.setSize(200,30);
		size[0].setLocation(55,50);
		size[0].setSize(75,30);
		size[1].setLocation(185,50);
		size[1].setSize(75,30);

		//공 속도 위치지정
		cspeed.setLocation(135,120);
		cspeed.setSize(200,30);
		for(int i=0; i<speed.length; i++){
			speed[i].setLocation(20+70*i,150);
			speed[i].setSize(60,30);
		}

		//확인 취소 위치 지정
		ok.setLocation(55,220);
		ok.setSize(75,30);
		cancel.setLocation(185,220);
		cancel.setSize(75,30);

		//버튼 추가
		cp.add(csize);
		cp.add(cspeed);
		cp.add(ok);
		cp.add(cancel);
		for(int i=0; i<size.length; i++){
			cp.add(size[i]);
		}
		for(int i=0; i<speed.length; i++){
			cp.add(speed[i]);
		}


		setSize(350,300);
		setVisible(true);
	}
	//예제 11-6
	class SpListener implements ItemListener {
		public void itemStateChanged(ItemEvent e){
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return; //선택 해제된 경우 그냥 리턴
			if(speed[0].isSelected())
				sp = 0;
			else if(speed[1].isSelected())
				sp = 1;
			else if(speed[2].isSelected())
				sp = 2;
			else
				sp = 3;
		}
	}

	class SzListener implements ItemListener {
		public void itemStateChanged(ItemEvent e){
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return; //선택 해제된 경우 그냥 리턴
			if(size[0].isSelected())
				sz = 0;
			else
				sz = 1;
		}
	}
}

class StyledButtonUI extends BasicButtonUI {

	@Override
	public void installUI (JComponent c) {
		super.installUI(c);
		AbstractButton button = (AbstractButton) c;
		button.setOpaque(false);
		button.setBorder(new EmptyBorder(5, 15, 5, 15));
	}

	@Override
	public void paint (Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
		super.paint(g, c);
	}

	private void paintBackground (Graphics g, JComponent c, int yOffset) {
		Dimension size = c.getSize();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(c.getBackground().darker());
		g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
		g.setColor(c.getBackground());
		g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
	}
}
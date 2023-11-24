
package CLIENT.UI;

import CLIENT.Client;
import CLIENT.DTO.ExamDTO;
import CLIENT.DTO.UserDTO;
import CLIENT.UI.model.header;
import CLIENT.UI.model.navItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ManageExam extends JFrame implements MouseListener {
     private String userID;
    private String userName;
    private String role;
    private boolean flag = true;
    private JPanel header,nav;
    boolean blockListExam=false;
    boolean blockTakeExam=false;
    static UserDTO userGlobal; // lưu user khi đăng nhập
     /*
     status: 1: acive
     2:block thi
     3:block tao dề thi
     4: block luôn user
     
     
     */

    public static UserDTO getUser() {
        return userGlobal;
    }

    public void setUser(UserDTO user) {
        this.userGlobal = user;
    }
   
    static JPanel main;
    private static int DEFAULT_HEIGHT = 750,DEFALUT_WIDTH = 1290 ;
    private ArrayList<String> navItem = new ArrayList<>();  //Chứa thông tin có button cho menu gồm
    private ArrayList<navItem> navObj = new ArrayList<>();  //Chứa cái button trên thanh menu
    public ManageExam(String userID, String userName, String role,UserDTO user) throws Exception
    {
        this.userID = userID;
        this.userName = userName;
        this.role = role;
        setUser(user);
        Toolkit screen = Toolkit.getDefaultToolkit();
        init();
        initRole();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public ManageExam() throws Exception
    {
        Toolkit screen = Toolkit.getDefaultToolkit();
        init();
        initRole();
         setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void initRole(){
       int status=userGlobal.getStatus();
       if (status!=1 ) {
           blockTakeExam=(status==2 || status==4)?true:false;
           blockListExam=(status==3 || status==4)?true:false;
       } 
    }
    private void freeVariable() {
        blockListExam = false;
        blockTakeExam = false;
        userGlobal = null;
        
    }
    public static void setSayName(header hmain,Font font,String name){
          JLabel user = new JLabel("Chào, "+name);
             user.setFont(font);
            user.setForeground(Color.WHITE);
            user.setBounds(new Rectangle(DEFALUT_WIDTH-270,-7,150,50));
            hmain.add(user);
    }
    public void init() throws Exception
    {
        Font font = new Font("Segoe UI",Font.BOLD,14);
        setTitle("Quản Lý Cửa Hàng Sách ");
        ImageIcon logo = new ImageIcon("./src/main/java/CLIENT/UI/img/test-passed-100.png");
        setIconImage(logo.getImage());
        setLayout(new BorderLayout());
        setSize(DEFALUT_WIDTH,DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        //setShape(new RoundRectangle2D.Double(0, 0, DEFALUT_WIDTH, DEFAULT_HEIGHT, 30, 30)); //Bo khung Frame
        
/************ PHẦN HEADER *************************************/      
        header = new JPanel(null);
        header.setBackground(new Color(27, 27, 30));
        header.setPreferredSize(new Dimension(DEFALUT_WIDTH,40));
        
        header hmain = new header(DEFALUT_WIDTH,40);
        
        if(userName != null)
        {
            if(role.compareToIgnoreCase("Admin")==0) userName = " Admin";
          //  setSayName(hmain,font,userGlobal.getName());
          JLabel user = new JLabel("Chào, "+userGlobal.getName());
             user.setFont(font);
            user.setForeground(Color.WHITE);
            user.setBounds(new Rectangle(DEFALUT_WIDTH-270,-7,150,50));
            hmain.add(user);
            
            
            //Tạo btn Logout
            navItem btnLogOut = new navItem("", new Rectangle(DEFALUT_WIDTH-150, -8, 50, 50), "logout_25px.png", "logout_25px.png", "logout_hover_25px.png", new Color(80, 80, 80));
            hmain.add(btnLogOut.isButton());
            btnLogOut.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e)
               {
           
                   try {
                       freeVariable();
                       ///Client.OutClient();
                       DangNhapUI lg = new DangNhapUI();
                       
                       dispose();
                   } catch (IOException ex) {
                       ex.printStackTrace();
                       Logger.getLogger(ManageExam.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (NoSuchAlgorithmException ex) {
                       throw new RuntimeException(ex);
                   }


               }
            });
        }
        
        //Tạo btn EXIT & MINIMIZE
        navItem exit = new navItem("", new Rectangle(DEFALUT_WIDTH-50, -8, 50, 50), "exit_25px.png", "exit_25px.png", "exit_hover_25px.png", new Color(240, 71, 74));
        navItem minimize = new navItem("", new Rectangle(DEFALUT_WIDTH-100, -8, 50, 50), "minimize_25px.png", "minimize_25px.png", "minimize_hover_25px.png" ,new Color(80,80,80));
        
        hmain.add(exit.isButton());
        hmain.add(minimize.isButton());
        
        exit.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent e)
           {
               try {
                   Client.OutClient();
               } catch (IOException ex) {
                   Logger.getLogger(ManageExam.class.getName()).log(Level.SEVERE, null, ex);
               }
              System.exit(0);
           }
        });
        
        minimize.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent e)
           {
              setState(Frame.ICONIFIED);
           }
        });
        
        header.add(hmain);
        
/****************************************************************/    


/************ PHẦN NAVIGATION ( MENU ) **************************/  
        nav = new JPanel(null);
        nav.setBackground(new Color(55, 63, 81));
        nav.setPreferredSize(new Dimension(180,DEFAULT_HEIGHT));
        
        JScrollPane scroll = new JScrollPane(nav);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(1,100));
        scroll.setHorizontalScrollBarPolicy(scroll.HORIZONTAL_SCROLLBAR_NEVER);
     
        
        //Thêm item vào thanh menu (Tên item : icon : icon hover)
        navItem = new ArrayList<>();  //Chứa thông tin có button cho menu gồm ( Tên btn : icon : icon hover )
          if(userGlobal.getStatus()==1) navItemMain();
          else if(userGlobal.getStatus()==2) navItemBlockTakeExam();
          else navItemBlockManaExam();
            

        outNav();
        
/************ PHẦN MAIN ( HIỂN THỊ ) **************************/        
        main = new JPanel(null);
        main.setBackground(Color.WHITE);
        navObj.get(0).doActive();
        changeMainInfo(0);
/**************************************************************/   

        add(header,BorderLayout.NORTH);
        add(scroll,BorderLayout.WEST);
        add(main,BorderLayout.CENTER);
      
        setVisible(true);
    }
    public void navItemMain() {
        navItem.add("Quản Lý Đề Thi:QLSP_20px.png:QLSP_20px_active.png");
        navItem.add("Thi:ThongKe_20px.png:ThongKe_20px_active.png");
        navItem.add("Người Dùng:KhachHang_20px.png:KhachHang_20px_active.png");
        navItem.add("Thống Kê:ThongKe_20px.png:ThongKe_20px_active.png");
    }

    public void navItemBlockTakeExam() {
        navItem.add("Quản Lý Đề Thi:QLSP_20px.png:QLSP_20px_active.png");
        navItem.add("Người Dùng:KhachHang_20px.png:KhachHang_20px_active.png");
        navItem.add("Thống Kê:ThongKe_20px.png:ThongKe_20px_active.png");
    }

    public void navItemBlockManaExam() {
        navItem.add("Thi:ThongKe_20px.png:ThongKe_20px_active.png");
        navItem.add("Người Dùng:KhachHang_20px.png:KhachHang_20px_active.png");
        navItem.add("Thống Kê:ThongKe_20px.png:ThongKe_20px_active.png");
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        for(int i  = 0 ; i<navObj.size();i++)
        {
            navItem item = navObj.get(i); // lấy vị trí item trong menu
            if(e.getSource()== item)
            {
                item.doActive(); // Active NavItem đc chọn 
                try {
                    if (blockListExam)
                        changeMainInfoManageExam(i);
                    else if (blockTakeExam)
                        changeMainInfoBlockTakeExam(i);
                    else
                    changeMainInfo(i); // Hiển thị ra phần main
                } catch (Exception ex) {
                    Logger.getLogger(ManageExam.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                item.noActive();
            }
        }
    }

    public JPanel getMain() {
        return main;
    }

    public void setMain(JPanel main) {
        ManageExam.main = main;
    }
  
    public static void openTakeExamPanel(ExamDTO exam,UserDTO user) throws Exception {
                main.removeAll();

     //   UserDTO user = new UserDTO(1,"minhtrung436@gmail.com","123","Trung",true,true,new Timestamp(System.currentTimeMillis()),"user");
                main.add(new TakeExam(DEFALUT_WIDTH,exam,user));
                main.repaint();
                main.revalidate();
    }
    public static void openBeginExamPanel(ExamDTO exam,UserDTO user){
                main.removeAll();
                main.add(new BeginExam(DEFALUT_WIDTH, exam,user));
                main.repaint();
                main.revalidate();
    }
    public static void openChooenExamPanel(UserDTO user){
                main.removeAll();
                main.add(new ChooseExam(DEFALUT_WIDTH,user));
                main.repaint();
                main.revalidate();
    }
     public static void openCreateExamPanel(ExamDTO exam,UserDTO user){
                main.removeAll();
                main.add(new CreateExam(DEFALUT_WIDTH,exam,user));
                main.repaint();
                main.revalidate();
    }
       public static void openListExamPanel(ExamDTO exam,UserDTO user){
                main.removeAll();
                main.add(new ListExam(DEFALUT_WIDTH, userGlobal));
                main.repaint();
                main.revalidate();
    }
//     int status=userGlobal.getStatus();
//       boolean blockListExam=false;
//       boolean blockTakeExam=false;
//       if (status!=1 ) {
//           blockTakeExam=(status==2 || status==4)?true:false;
//           blockListExam=(status==3 || status==4)?true:false;
//       } 
//       int listExam=0,ichoosenExam=1,profile=2,Satistic=3;
    public void changeMainInfo(int i) throws Exception //Đổi Phần hiển thị khi bấm btn trên menu
    {
        switch(i)
        {
            case 0: 
                main.removeAll();
                main.add(new ListExam(DEFALUT_WIDTH, userGlobal));
                main.repaint();
                main.revalidate();
            break;
            case 1: 
                main.removeAll();
                main.add(new ChooseExam(DEFALUT_WIDTH,userGlobal));
                main.repaint();
                main.revalidate();
            break;
            case 2: 
                main.removeAll();
                main.add(new MyProfileUI(DEFALUT_WIDTH,Integer.parseInt(userID)));
                main.repaint();
                main.revalidate();
            break;
            case 3: 
                main.removeAll();
                main.add(new StatisticExam(DEFALUT_WIDTH));
                main.repaint();
                main.revalidate();
            break;
            default:
                break;
           
        }
    }
    public void changeMainInfoManageExam(int i) throws Exception //Đổi Phần hiển thị khi bấm btn trên menu
    {
        switch(i)
        {
            case 0: 
                main.removeAll();
                main.add(new ChooseExam(DEFALUT_WIDTH,userGlobal));
                main.repaint();
                main.revalidate();
            break;
            case 1: 
                main.removeAll();
                main.add(new MyProfileUI(DEFALUT_WIDTH,Integer.parseInt(userID)));
                main.repaint();
                main.revalidate();
            break;
            case 2: 
                main.removeAll();
                main.add(new StatisticExam(DEFALUT_WIDTH));
                main.repaint();
                main.revalidate();
            break;
            default:
                break;
           
        }
    }
    public void changeMainInfoBlockTakeExam(int i) throws Exception //Đổi Phần hiển thị khi bấm btn trên menu
    {
        switch(i)
        {
            case 0: 
                main.removeAll();
                main.add(new ListExam(DEFALUT_WIDTH, userGlobal));
                main.repaint();
                main.revalidate();
            break;
            case 1: 
                main.removeAll();
                main.add(new MyProfileUI(DEFALUT_WIDTH,Integer.parseInt(userID)));
                main.repaint();
                main.revalidate();
            break;
            case 2: 
                main.removeAll();
                main.add(new StatisticExam(DEFALUT_WIDTH));
                main.repaint();
                main.revalidate();
            break;
            default:
                break;
           
        }
    }
    
    
    public void outNav()
    {
        //Gắn cái NavItem vào NavOBJ
        navObj.clear();
        for(int i = 0 ; i < navItem.size() ; i++)
        {
            String s = navItem.get(i).split(":")[0];
            String icon = navItem.get(i).split(":")[1];
            String iconActive = navItem.get(i).split(":")[2];
            navObj.add(new navItem(s, new Rectangle(0,200+50*i,210,50),icon,iconActive));
            navObj.get(i).addMouseListener(this);
        }
     
        
        //Xuất ra Naigation
        nav.removeAll();
        JLabel profile = new JLabel(new ImageIcon("./src/main/java/CLIENT/UI/img/test-passed-100.png"));
        profile.setBounds(0,0,210,200);
        nav.add(profile);
        for(navItem n : navObj)
        {
            nav.add(n); 
        }
        repaint();
        revalidate();
    }
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {

    }
    
}


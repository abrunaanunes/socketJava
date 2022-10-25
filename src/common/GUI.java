package common;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class GUI extends JFrame{
    public GUI(String title){
        super(title);
        initComponents();
        configComponents();
        insertComponents();
        insertActions();
        start();
    }
    protected abstract void initComponents();
    protected abstract void configComponents();
    protected abstract void insertComponents();
    protected abstract void insertActions();
    protected abstract void start();
}
package calc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int frustrationCount;

	private double input_c; //Input parsed into a double.
	private int input_whole; //Whole part of the input.
	private int input_dec = 0; //Decimal part of the input.
	private boolean input_isDouble; //If decimal part is not empty, it's a double.
	
	private String opCode; //Calculation code.
	private double answer;
	
	private JLabel operationDisplay; //Where the calculation to do will be displayed.
	private JLabel inputDisplay; //Where the input/answer will be displayed.
	
	class KeyboardListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			int c = e.getKeyCode();
			for (int i = 0; i < 10; i++) {
				// +48 are normal, ASCII numbers (the one on top of the keyboard's letters).
				// +96 are numpad numbers.
				if(c == i + 48 || c == i + 96) {
					appendNumber(i);
				}
			}
			//Delete a number
			if (c == KeyEvent.VK_BACK_SPACE)
				deleteNumber();
			if (c == KeyEvent.VK_COMMA || c == KeyEvent.VK_PERIOD || c == KeyEvent.VK_DECIMAL)
				appendComma();
			if (c == KeyEvent.VK_PLUS || c == KeyEvent.VK_ADD)
				plusBtn();
			if (c == KeyEvent.VK_ENTER || c == KeyEvent.VK_ACCEPT)
				equalsBtn();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public MainWindow() {
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

		Font operationFont = new Font("SansSerif", Font.PLAIN, 18);
		operationDisplay = new JLabel(0 + "");
		operationDisplay.setMaximumSize(new Dimension(Integer.MAX_VALUE,24));
		operationDisplay.setHorizontalAlignment(JTextField.TRAILING);
		operationDisplay.setFont(operationFont);
		cp.add(operationDisplay);
		
		cp.add(Box.createRigidArea(new Dimension(0, 3))); //Separator
		
		Font answerFont = new Font("Segoe", Font.BOLD, 48);
		inputDisplay = new JLabel(0 + "");
		inputDisplay.setHorizontalAlignment(JTextField.TRAILING);
		inputDisplay.setMinimumSize(new Dimension(700, 100));
		inputDisplay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		inputDisplay.setFont(answerFont);
		inputDisplay.setFocusable(true);
		inputDisplay.addKeyListener(new KeyboardListener());
		cp.add(inputDisplay);
		
		cp.add(Box.createRigidArea(new Dimension(0, 3))); //Separator
		
		JPanel numPad = new JPanel();
		numPad.setLayout(new GridLayout(4, 3));
		int i = 9;
		for (i = 9; i > 0; i--) {
			JButton btn = new JButton(i + "");
			Font numFont = new Font("SansSerif", Font.BOLD, 32);
			btn.setFont(numFont);
			
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					if(!input_isDouble) {
						input_whole = Integer.valueOf(inputDisplay.getText());
					} else {
						//Try to parse the decimal part of the input. If the user just wrote the decimal point, ignore.
						try {
							input_dec = Integer.valueOf(inputDisplay.getText().split("[.]")[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							
						}
					}
					appendNumber(Integer.valueOf(btn.getText()));
					inputDisplay.requestFocus();
		         }
			});
			numPad.add(btn);
		}

		JButton commaBtn = new JButton(".");
		commaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				appendComma();
				inputDisplay.requestFocus();
			}
		});
		JButton plusBtn = new JButton("+");
		plusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				plusBtn();
				inputDisplay.requestFocus();
			}
		});
		JButton equalsBtn = new JButton("=");
		equalsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				equalsBtn();
				inputDisplay.requestFocus();
			}
		});
		numPad.add(commaBtn);
		numPad.add(plusBtn);
		numPad.add(equalsBtn);
		cp.add(numPad);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Calculator");
		setResizable(false);
		setSize(400, 500);
		setVisible(true);
		cp.validate();
	}
	
	public void plusBtn() {
		input_c = MakeDouble(input_whole, input_dec);
		answer = input_c;
		opCode = answer + "+";
		operationDisplay.setText(opCode);
		input_c = 0;
		input_whole = 0;
		input_dec = 0;
		input_isDouble = false;
		answer = 0;
		inputDisplay.setText("0");
	}
	
	public void equalsBtn() {
		input_c = MakeDouble(input_whole, input_dec);
		opCode += input_c;
		
		inputDisplay.setText(doCalculation() + "");
		operationDisplay.setText(opCode);
		answer = doCalculation();
	}
	
	public void appendComma() {
		//Only input a comma if one hasn't been input before.
		if (!input_isDouble) {
			input_isDouble = true;
			inputDisplay.setText(input_whole + ".");
		} else {
			frustrationCount++;
			if (frustrationCount > 10) {
				frustrationCount = 0;
				JOptionPane.showMessageDialog(getContentPane(), "You've already written a decimal point!", "Remember!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void appendNumber(int n) {
		String volatileNum;
		int num;
		
		if (!input_isDouble) {
			num = input_whole;
		} else {
			num = input_dec;
		}
		
		volatileNum = num + "";
		volatileNum += n;
		
		try {
			num = Integer.valueOf(volatileNum);
		} catch (NumberFormatException e) {
			//If the user tries to input a number higher than Integer type's max value, warn them.
			frustrationCount++;
			if (frustrationCount > 10) {
				frustrationCount = 0;
				JOptionPane.showMessageDialog(getContentPane(), "Number can't be bigger than " + Integer.MAX_VALUE + ".", "Remember!", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		if(!input_isDouble) {
			input_whole = num;
			inputDisplay.setText(input_whole + "");
		} else {
			input_dec = num;
			inputDisplay.setText(input_whole + "." + input_dec);
		}
	}
	
	public void deleteNumber() {
		String ans = answer + "";
		if (ans.length() > 1)
			answer = Integer.valueOf(ans.subSequence(0, ans.length() - 1).toString());
		else
			answer = 0;
		inputDisplay.setText(answer + "");
	}
	
	double MakeDouble (int wholePart, int decimalPart) {
		if (decimalPart == 0) {
			return wholePart;
		} else {
			return wholePart + (decimalPart / Math.pow(10, (Math.floor(Math.log10(decimalPart)) + 1)));
		}
	}
	
	public double doCalculation() {
		//How to do concatenated operations? Recursion?
		answer = 0;
		
		if (opCode.contains("+")) {
			return Double.valueOf(opCode.split("[+]")[0]) +  Double.valueOf(opCode.split("[+]")[1]);
		} else {
			return answer;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("REMEMBER: The maximum integer is " + Integer.MAX_VALUE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow();
			}
		});
	}

}

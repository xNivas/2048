package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    SwipeListener swipeListener;
    TextView cell00, cell01, cell02, cell03,
            cell10, cell11, cell12, cell13,
            cell20, cell21, cell22, cell23,
            cell30, cell31, cell32, cell33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell00 = (TextView) findViewById(R.id.cell00);
        cell01 = (TextView) findViewById(R.id.cell01);
        cell02 = (TextView) findViewById(R.id.cell02);
        cell03 = (TextView) findViewById(R.id.cell03);

        cell10 = (TextView) findViewById(R.id.cell10);
        cell11 = (TextView) findViewById(R.id.cell11);
        cell12 = (TextView) findViewById(R.id.cell12);
        cell13 = (TextView) findViewById(R.id.cell13);

        cell20 = (TextView) findViewById(R.id.cell20);
        cell21 = (TextView) findViewById(R.id.cell21);
        cell22 = (TextView) findViewById(R.id.cell22);
        cell23 = (TextView) findViewById(R.id.cell23);

        cell30 = (TextView) findViewById(R.id.cell30);
        cell31 = (TextView) findViewById(R.id.cell31);
        cell32 = (TextView) findViewById(R.id.cell32);
        cell33 = (TextView) findViewById(R.id.cell33);

        TextView[][] cells;
        cells = new TextView[][]{{cell00, cell01, cell02, cell03},
                {cell10, cell11, cell12, cell13},
                {cell20, cell21, cell22, cell23},
                {cell30, cell31, cell32, cell33}};

//        initiateCells();
//        assignCellsToList(cells);

        //Initialize layout for swipe listener
        layout = findViewById(R.id.layout);
        //Initialize swipe listener
        swipeListener = new SwipeListener(layout);
        initiateBoard(swipeListener, cells,"");

    }

//    private void assignCellsToList(TextView[] cells) {
//        cells.add(cell00);
//        cells.add(cell01);
//        cells.add(cell02);
//        cells.add(cell03);
//
//        cells.add(cell10);
//        cells.add(cell11);
//        cells.add(cell12);
//        cells.add(cell13);
//
//        cells.add(cell20);
//        cells.add(cell21);
//        cells.add(cell22);
//        cells.add(cell23);
//
//        cells.add(cell30);
//        cells.add(cell31);
//        cells.add(cell32);
//        cells.add(cell33);
//    }

//    private void initiateCells() {
//    }

    private void initiateBoard(SwipeListener swipeListener, TextView[][] cells, String value) {
        Board board = new Board();
        swipeListener.setBoard(board);
        swipeListener.setCells(cells);
        for (int i = 0; i != 4; i++) {
            for (int j = 0; j != 4; j++) {
                cells[i][j].setText(value);
            }
        }
        generateStartingCells(cells, board);
    }


    private void generateStartingCells(TextView[][] cells, Board board) {
        int random1 = new Random().nextInt(4);
        int random2 = new Random().nextInt(4);
        cells[random1][random2].setText("2");
        board.setSingleValueOnBoard(random1,random2,2);
        int random3 = new Random().nextInt(4);
        int random4 = new Random().nextInt(4);
        while(random1==random3 && random2==random4) {
            random3 = new Random().nextInt(4);
            random4 = new Random().nextInt(4);
        }
        cells[random3][random4].setText("2");
        board.setSingleValueOnBoard(random3,random4,2);
    }

    private class SwipeListener implements View.OnTouchListener {
        //Initialize variable
        GestureDetector gestureDetector;
        Board board;
        TextView[][] cells;

        public void setCells(TextView[][] cells) {
            this.cells = cells;
        }
        public void setBoard(Board board) {
            this.board = board;
        }

        //Create constructor
        SwipeListener(View view) {
            //Initialize threshold value
            int threshold = 100;
            int velocity_threshold = 100;

            //Initialize simple gesturelistener
            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener(){
                        @Override
                        public boolean onDown(MotionEvent e) {
                            //Pass true value
                            return true;
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            float xDiff = e2.getX() - e1.getX();
                            float yDiff = e2.getY() - e1.getY();
                            try {
                                if(Math.abs(xDiff)>Math.abs(yDiff)) {
                                    //When x is greater than y
                                    //Check condition
                                    if (Math.abs(xDiff) > threshold
                                            && Math.abs(velocityX) > velocity_threshold) {
                                        if (xDiff>0) {
                                            Log.d("appLogs", "Swiped Right");
                                            onSwipedRight(board, cells);
                                        } else {
                                            Log.d("appLogs","Swiped left");
                                            onSwipedLeft(board,cells);
                                        }
                                        return true;
                                    }
                                } else {
                                    if (Math.abs(yDiff) > threshold
                                            && Math.abs(velocityY) > velocity_threshold) {
                                        //When y difference is greater than threshold
                                        //When y velocity is greater than velocity threshold
                                        if (yDiff > 0) {
                                            //When swiped down
                                            Log.d("appLogs", "Swiped Down");
                                            onSwipedDown(board,cells);
                                        } else {
                                            Log.d("appLogs", "Swiped Up");
                                            onSwipedUp(board,cells);
                                        }
                                        return true;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
            //Initialize gesture detector
            gestureDetector = new GestureDetector(listener);
            //Set listener on view
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }

    private void onSwipedRight(Board board, TextView[][] cells) {
        modifyRowToRight(0,cells,board);
        modifyRowToRight(1,cells,board);
        modifyRowToRight(2,cells,board);
        modifyRowToRight(3,cells,board);
        newNumber(cells, board);
    }
    private void onSwipedLeft(Board board, TextView[][] cells) {
        modifyRowToLeft(0,cells,board);
        modifyRowToLeft(1,cells,board);
        modifyRowToLeft(2,cells,board);
        modifyRowToLeft(3,cells,board);
        newNumber(cells, board);
    }
    private void onSwipedDown(Board board, TextView[][] cells) {
        modifyColumnDown(0,cells,board);
        modifyColumnDown(1,cells,board);
        modifyColumnDown(2,cells,board);
        modifyColumnDown(3,cells,board);
        newNumber(cells, board);
    }
    private void onSwipedUp(Board board, TextView[][] cells) {
        modifyColumnUp(0,cells,board);
        modifyColumnUp(1,cells,board);
        modifyColumnUp(2,cells,board);
        modifyColumnUp(3,cells,board);
        newNumber(cells, board);
    }
    private void modifyRowToLeft(int rowNumber,TextView[][] cells, Board board) {
        TextView cell0 = cells[rowNumber][0];
        TextView cell1 = cells[rowNumber][1];
        TextView cell2 = cells[rowNumber][2];
        TextView cell3 = cells[rowNumber][3];
        int col0 = board.getSingleValueFromBoard(rowNumber,0);
        int col1 = board.getSingleValueFromBoard(rowNumber,1);
        int col2 = board.getSingleValueFromBoard(rowNumber,2);
        int col3 = board.getSingleValueFromBoard(rowNumber,3);
        Log.d("appLogs", col0 + " " + col1 + " " + col2 + " " + col3);

        if (col3 == 0) {
            if (col2 == 0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOOO
                        //nothing
                    }
                    else if (col0 != 0) {
                        //XOOO
                        //nothing
                    }
                }
                else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXOO
                        board.setSingleValueOnBoard(rowNumber,1,0);
                        board.setSingleValueOnBoard(rowNumber,0,col1);
                        cell1.setText("");
                        cell0.setText(String.valueOf(col1));
                    }
                    else if (col0 != 0) {
                        //XXOO
                        if (col0==col1) {
                            board.setSingleValueOnBoard(rowNumber,0,col0+col1);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            cell0.setText(String.valueOf(col0+col1));
                            cell1.setText("");
                        } else {
                            //nothing
                        }
                    }
                }
            }
            else if (col2 !=0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOXO
                        board.setSingleValueOnBoard(rowNumber,2,0);
                        board.setSingleValueOnBoard(rowNumber,0,col2);
                        cell2.setText("");
                        cell0.setText(String.valueOf(col2));
                    }
                    else if (col0 != 0) {
                        //XOXO
                        if (col0==col2) {
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,0,col0+col2);
                            cell0.setText(String.valueOf(col0+col2));
                            cell2.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,1,col2);
                            cell2.setText("");
                            cell1.setText(String.valueOf(col2));
                        }
                    }
                }
                else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXXO
                        if (col1 == col2) {
                            board.setSingleValueOnBoard(rowNumber,0,col1+col2);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            cell0.setText(String.valueOf(col1+col2));
                            cell1.setText("");
                            cell2.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,col1);
                            board.setSingleValueOnBoard(rowNumber,1,col2);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col1));
                            cell1.setText(String.valueOf(col2));
                            cell2.setText("");
                            cell3.setText("");
                        }
                    }
                    else if (col0 != 0) {
                        //XXXO
                        if (col0==col1) {
                            board.setSingleValueOnBoard(rowNumber,0,col0+col1);
                            board.setSingleValueOnBoard(rowNumber,1,col2);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0+col1));
                            cell1.setText(String.valueOf(col2));
                            cell2.setText("");
                            cell3.setText("");
                        } else if (col1 == col2) {
                            board.setSingleValueOnBoard(rowNumber,1,col1+col2);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            cell1.setText(String.valueOf(col1+col2));
                            cell2.setText("");
                        } else {
                            //nothing
                        }
                    }
                }
            }
        }
        else if (col3 !=0) {
            if (col2 == 0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOOX
                        board.setSingleValueOnBoard(rowNumber,0,col3);
                        board.setSingleValueOnBoard(rowNumber,3,0);
                        cell0.setText(String.valueOf(col3));
                        cell3.setText("");
                    }
                    else if (col0 != 0) {
                        //XOOX
                        if (col0 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,col0+col3);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0+col3));
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,1,col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell1.setText(String.valueOf(col3));
                            cell2.setText("");
                            cell3.setText("");
                        }
                    }
                }
                else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXOX
                        if (col1 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,col1+col3);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col1+col3));
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,col1);
                            board.setSingleValueOnBoard(rowNumber,1,col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col1));
                            cell1.setText(String.valueOf(col3));
                            cell2.setText("");
                            cell3.setText("");
                        }
                    }
                    else if (col0 != 0) {
                        //XXOX
                        if (col0==col1) {
                            board.setSingleValueOnBoard(rowNumber,0,col0+col1);
                            board.setSingleValueOnBoard(rowNumber,1,col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0+col1));
                            cell1.setText(String.valueOf(col3));
                            cell2.setText("");
                            cell3.setText("");
                        } else if (col1==col3) {
                            board.setSingleValueOnBoard(rowNumber,0,col0);
                            board.setSingleValueOnBoard(rowNumber,1,col1+col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0));
                            cell1.setText(String.valueOf(col1+col3));
                            cell2.setText("");
                            cell3.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,col0);
                            board.setSingleValueOnBoard(rowNumber,1,col1);
                            board.setSingleValueOnBoard(rowNumber,2,col3);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0));
                            cell1.setText(String.valueOf(col1));
                            cell2.setText(String.valueOf(col3));
                            cell3.setText("");
                        }
                    }
                }
            }
            else if (col2 != 0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOXX
                        if (col2 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,col2+col3);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col2+col3));
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,col2);
                            board.setSingleValueOnBoard(rowNumber,1,col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col2));
                            cell1.setText(String.valueOf(col3));
                            cell2.setText("");
                            cell3.setText("");
                        }
                    }
                    else if (col0 != 0) {
                        //XOXX
                        if (col0 == col2) {
                            board.setSingleValueOnBoard(rowNumber,0,col0+col2);
                            board.setSingleValueOnBoard(rowNumber,1,col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0+col2));
                            cell1.setText(String.valueOf(col3));
                            cell2.setText("");
                            cell3.setText("");
                        } else if (col2 == col3) {
                            board.setSingleValueOnBoard(rowNumber,1,col2+col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell1.setText(String.valueOf(col2+col3));
                            cell2.setText("");
                            cell3.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,1,col2);
                            board.setSingleValueOnBoard(rowNumber,2,col3);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell1.setText(String.valueOf(col2));
                            cell2.setText(String.valueOf(col3));
                            cell3.setText("");
                        }
                    }
                } else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXXX
                        if (col1 == col2) {
                            board.setSingleValueOnBoard(rowNumber,0,col1);
                            board.setSingleValueOnBoard(rowNumber,1,col2);
                            board.setSingleValueOnBoard(rowNumber,2,col3);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col1));
                            cell1.setText(String.valueOf(col2));
                            cell2.setText(String.valueOf(col3));
                            cell3.setText("");
                        } else if (col2 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,col1);
                            board.setSingleValueOnBoard(rowNumber,1,col2+col3);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col1));
                            cell1.setText(String.valueOf(col2+col3));
                            cell2.setText("");
                            cell3.setText("");
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,col1);
                            board.setSingleValueOnBoard(rowNumber,1,col2);
                            board.setSingleValueOnBoard(rowNumber,2,col3);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col1));
                            cell1.setText(String.valueOf(col2));
                            cell2.setText(String.valueOf(col3));
                            cell3.setText("");
                        }
                    }
                    else if (col0 != 0) {
                        //XXXX
                        if(col0==col1) {
                            if(col2==col3) {
                                board.setSingleValueOnBoard(rowNumber,0,col0+col1);
                                board.setSingleValueOnBoard(rowNumber,1,col2+col3);
                                board.setSingleValueOnBoard(rowNumber,2,0);
                                board.setSingleValueOnBoard(rowNumber,3,0);
                                cell0.setText(String.valueOf(col0+col1));
                                cell1.setText(String.valueOf(col2+col3));
                                cell2.setText("");
                                cell3.setText("");
                            } else {
                                board.setSingleValueOnBoard(rowNumber,0,col0+col1);
                                board.setSingleValueOnBoard(rowNumber,1,col2);
                                board.setSingleValueOnBoard(rowNumber,2,col3);
                                board.setSingleValueOnBoard(rowNumber,3,0);
                                cell0.setText(String.valueOf(col0+col1));
                                cell1.setText(String.valueOf(col2));
                                cell2.setText(String.valueOf(col3));
                                cell3.setText("");
                            }
                        } else if (col1==col2) {
                            board.setSingleValueOnBoard(rowNumber,0,col0);
                            board.setSingleValueOnBoard(rowNumber,1,col1+col2);
                            board.setSingleValueOnBoard(rowNumber,2,col3);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0));
                            cell1.setText(String.valueOf(col1+col2));
                            cell2.setText(String.valueOf(col3));
                            cell3.setText("");
                        } else if (col2==col3) {
                            board.setSingleValueOnBoard(rowNumber,0,col0);
                            board.setSingleValueOnBoard(rowNumber,1,col1);
                            board.setSingleValueOnBoard(rowNumber,2,col2+col3);
                            board.setSingleValueOnBoard(rowNumber,3,0);
                            cell0.setText(String.valueOf(col0));
                            cell1.setText(String.valueOf(col1));
                            cell2.setText(String.valueOf(col2+col3));
                            cell3.setText("");
                        } else {
                            //nothing
                        }
                    }
                }
            }
        }
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        cell0.startAnimation(anim);
        cell1.startAnimation(anim);
        cell2.startAnimation(anim);
        cell3.startAnimation(anim);
    }

    private void modifyRowToRight(int rowNumber,TextView[][] cells, Board board) {
        TextView cell0 = cells[rowNumber][0];
        TextView cell1 = cells[rowNumber][1];
        TextView cell2 = cells[rowNumber][2];
        TextView cell3 = cells[rowNumber][3];
        int col0 = board.getSingleValueFromBoard(rowNumber,0);
        int col1 = board.getSingleValueFromBoard(rowNumber,1);
        int col2 = board.getSingleValueFromBoard(rowNumber,2);
        int col3 = board.getSingleValueFromBoard(rowNumber,3);
        Log.d("appLogs", col0 + " " + col1 + " " + col2 + " " + col3);

        if (col3 == 0) {
            if (col2 == 0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOOO
                    }
                    else if (col0 != 0) {
                        //XOOO
                        board.setSingleValueOnBoard(rowNumber,0,0);
                        board.setSingleValueOnBoard(rowNumber,3,col0);
                        cell0.setText("");
                        cell3.setText(String.valueOf(col0));
                    }
                }
                else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXOO
                        board.setSingleValueOnBoard(rowNumber,1,0);
                        board.setSingleValueOnBoard(rowNumber,3,col1);
                        cell1.setText("");
                        cell3.setText(String.valueOf(col1));
                    }
                    else if (col0 != 0) {
                        //XXOO
                        if (col0==col1) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,3,col0+col1);
                            cell0.setText("");
                            cell1.setText("");
                            cell3.setText(String.valueOf(col0+col1));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0);
                            board.setSingleValueOnBoard(rowNumber,3,col1);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0));
                            cell3.setText(String.valueOf(col1));
                        }
                    }
                }
            }
            else if (col2 !=0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOXO
                        board.setSingleValueOnBoard(rowNumber,0,0);
                        board.setSingleValueOnBoard(rowNumber,1,0);
                        board.setSingleValueOnBoard(rowNumber,2,0);
                        board.setSingleValueOnBoard(rowNumber,3,col2);
                        cell0.setText("");
                        cell1.setText("");
                        cell2.setText("");
                        cell3.setText(String.valueOf(col2));
                    }
                    else if (col0 != 0) {
                        //XOXO
                        if (col0==col2) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,col0+col2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(col0+col2));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0);
                            board.setSingleValueOnBoard(rowNumber,3,col2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0));
                            cell3.setText(String.valueOf(col2));
                        }
                    }
                }
                else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXXO
                        if (col1 == col2) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,col1+col2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(col1+col2));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col1);
                            board.setSingleValueOnBoard(rowNumber,3,col2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col1));
                            cell3.setText(String.valueOf(col2));
                        }
                    }
                    else if (col0 != 0) {
                        //XXXO
                        if (col1==col2) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0);
                            board.setSingleValueOnBoard(rowNumber,3,col1+col2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0));
                            cell3.setText(String.valueOf(col1+col2));
                        } else if (col0 == col1) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0+col1);
                            board.setSingleValueOnBoard(rowNumber,3,col2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0+col1));
                            cell3.setText(String.valueOf(col2));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,col0);
                            board.setSingleValueOnBoard(rowNumber,2,col1);
                            board.setSingleValueOnBoard(rowNumber,3,col2);
                            cell0.setText("");
                            cell1.setText(String.valueOf(col0));
                            cell2.setText(String.valueOf(col1));
                            cell3.setText(String.valueOf(col2));
                        }
                    }
                }
            }
        }
        else if (col3 !=0) {
            if (col2 == 0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOOX
                        //nothing
                    }
                    else if (col0 != 0) {
                        //XOOX
                        if (col0 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,col0+col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(col0+col3));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0);
                            board.setSingleValueOnBoard(rowNumber,3,col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0));
                            cell3.setText(String.valueOf(col3));
                        }
                    }
                }
                else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXOX
                        if (col1 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,col1+col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(col1+col3));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col1);
                            board.setSingleValueOnBoard(rowNumber,3,col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col1));
                            cell3.setText(String.valueOf(col3));
                        }
                    }
                    else if (col0 != 0) {
                        //XXOX
                        if (col1==col3) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0);
                            board.setSingleValueOnBoard(rowNumber,3,col1+col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0));
                            cell3.setText(String.valueOf(col1+col3));
                        } else if (col0==col1) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0+col1);
                            board.setSingleValueOnBoard(rowNumber,3,col3);
                            cell0.setText(0);
                            cell1.setText(0);
                            cell2.setText(String.valueOf(col0+col1));
                            cell3.setText(String.valueOf(col3));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,col0);
                            board.setSingleValueOnBoard(rowNumber,2,col1);
                            cell0.setText("");
                            cell1.setText(String.valueOf(col0));
                            cell2.setText(String.valueOf(col1));
                        }
                    }
                }
            }
            else if (col2 != 0) {
                if (col1 == 0) {
                    if (col0 == 0) {
                        //OOXX
                        if (col2 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,0);
                            board.setSingleValueOnBoard(rowNumber,3,col2+col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(col2+col3));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col2);
                            board.setSingleValueOnBoard(rowNumber,3,col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col2));
                            cell3.setText(String.valueOf(col3));
                        }
                    }
                    else if (col0 != 0) {
                        //XOXX
                        if (col2 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0);
                            board.setSingleValueOnBoard(rowNumber,3,col2+col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0));
                            cell3.setText(String.valueOf(col2+col3));
                        } else if (col0 == col2) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col0+col2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col0+col2));
                        } else {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,col0);
                            cell0.setText("");
                            cell1.setText(String.valueOf(col0));
                        }
                    }
                } else if (col1 != 0) {
                    if (col0 == 0) {
                        //OXXX
                        if (col2 == col3) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col1);
                            board.setSingleValueOnBoard(rowNumber,3,col2+col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col1));
                            cell3.setText(String.valueOf(col2+col3));
                        } else if (col1 == col2) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,0);
                            board.setSingleValueOnBoard(rowNumber,2,col1+col2);
                            board.setSingleValueOnBoard(rowNumber,3,col3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(col1+col2));
                            cell3.setText(String.valueOf(col3));
                        } else {

                        }
                    }
                    else if (col0 != 0) {
                        //XXXX
                        if(col2==col3) {
                            if(col0==col1) {
                                board.setSingleValueOnBoard(rowNumber,0,0);
                                board.setSingleValueOnBoard(rowNumber,1,0);
                                board.setSingleValueOnBoard(rowNumber,2,col0+col1);
                                board.setSingleValueOnBoard(rowNumber,3,col2+col3);
                                cell0.setText("");
                                cell1.setText("");
                                cell2.setText(String.valueOf(col0+col1));
                                cell3.setText(String.valueOf(col2+col3));
                            } else {
                                board.setSingleValueOnBoard(rowNumber,0,0);
                                board.setSingleValueOnBoard(rowNumber,1,col0);
                                board.setSingleValueOnBoard(rowNumber,2,col1);
                                board.setSingleValueOnBoard(rowNumber,3,col2+col3);
                                cell0.setText("");
                                cell1.setText(String.valueOf(col0));
                                cell2.setText(String.valueOf(col1));
                                cell3.setText(String.valueOf(col2+col3));
                            }
                        } else if (col1==col2) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,col0);
                            board.setSingleValueOnBoard(rowNumber,2,col1+col2);
                            board.setSingleValueOnBoard(rowNumber,3,col3);
                            cell0.setText("");
                            cell1.setText(String.valueOf(col0));
                            cell2.setText(String.valueOf(col1+col2));
                            cell3.setText(String.valueOf(col3));
                        } else if (col0==col1) {
                            board.setSingleValueOnBoard(rowNumber,0,0);
                            board.setSingleValueOnBoard(rowNumber,1,col0+col1);
                            board.setSingleValueOnBoard(rowNumber,2,col2);
                            board.setSingleValueOnBoard(rowNumber,3,col3);
                            cell0.setText("");
                            cell1.setText(String.valueOf(col0+col1));
                            cell2.setText(String.valueOf(col2));
                            cell3.setText(String.valueOf(col3));
                        } else {
                            //nothing
                        }
                    }
                }
            }
        }
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        cell0.startAnimation(anim);
        cell1.startAnimation(anim);
        cell2.startAnimation(anim);
        cell3.startAnimation(anim);
    }

    private void modifyColumnDown(int columnNumber, TextView[][] cells, Board board) {
        //cells 0 1 2 3 from up to down
        TextView cell0 = cells[0][columnNumber];
        TextView cell1 = cells[1][columnNumber];
        TextView cell2 = cells[2][columnNumber];
        TextView cell3 = cells[3][columnNumber];
        int row0 = board.getSingleValueFromBoard(0,columnNumber);
        int row1 = board.getSingleValueFromBoard(1,columnNumber);
        int row2 = board.getSingleValueFromBoard(2,columnNumber);
        int row3 = board.getSingleValueFromBoard(3,columnNumber);
        Log.d("appLogs", row0 + " " + row1 + " " + row2 + " " + row3);

        if (row3 == 0) {
            if (row2 == 0) {
                if (row1 == 0) {
                    if (row0 == 0) {
                        //O
                        //O
                        //O
                        //O
                        //nothing
                    }
                    else if (row0 != 0) {
                        //X
                        //O
                        //O
                        //O
                        board.setSingleValueOnBoard(0,columnNumber,0);
                        board.setSingleValueOnBoard(3,columnNumber,row0);
                        cell0.setText("");
                        cell3.setText(String.valueOf(row0));
                    }
                }
                else if (row1 != 0) {
                    if (row0 == 0) {
                        //O
                        //X
                        //O
                        //O
                        board.setSingleValueOnBoard(1,columnNumber,0);
                        board.setSingleValueOnBoard(3,columnNumber,row0);
                        cell1.setText("");
                        cell3.setText(String.valueOf(row1));
                    }
                    else if (row0 != 0) {
                        //X
                        //X
                        //O
                        //O
                        if (row0==row1) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,0);
                            board.setSingleValueOnBoard(3,columnNumber,row0+row1);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(row0+row1));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0);
                            board.setSingleValueOnBoard(3,columnNumber,row1);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0));
                            cell3.setText(String.valueOf(row1));
                        }
                    }
                }
            }
            else if (row2 !=0) {
                if (row1 == 0) {
                    if (row0 == 0) {
                        //O
                        //O
                        //X
                        //O
                        board.setSingleValueOnBoard(2,columnNumber,0);
                        board.setSingleValueOnBoard(3,columnNumber,row2);
                        cell2.setText("");
                        cell3.setText(String.valueOf(row2));
                    }
                    else if (row0 != 0) {
                        //X
                        //O
                        //X
                        //O
                        if (row0==row2) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,0);
                            board.setSingleValueOnBoard(3,columnNumber,row0+row2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(row0+row2));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0);
                            board.setSingleValueOnBoard(3,columnNumber,row2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0));
                            cell3.setText(String.valueOf(row2));
                        }
                    }
                }
                else if (row1 != 0) {
                    if (row0 == 0) {
                        //O
                        //X
                        //X
                        //O
                        if (row1 == row2) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,0);
                            board.setSingleValueOnBoard(3,columnNumber,row1+row2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(row1+row2));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row1);
                            board.setSingleValueOnBoard(3,columnNumber,row2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row1));
                            cell3.setText(String.valueOf(row2));
                        }
                    }
                    else if (row0 != 0) {
                        //X
                        //X
                        //X
                        //O
                        if (row1==row2) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0);
                            board.setSingleValueOnBoard(3,columnNumber,row1+row2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0));
                            cell3.setText(String.valueOf(row1+row2));
                        } else if (row0 == row1) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0+row1);
                            board.setSingleValueOnBoard(3,columnNumber,row2);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0+row1));
                            cell3.setText(String.valueOf(row2));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,row0);
                            board.setSingleValueOnBoard(2,columnNumber,row1);
                            board.setSingleValueOnBoard(3,columnNumber,row2);
                            cell0.setText("");
                            cell1.setText(String.valueOf(row0));
                            cell2.setText(String.valueOf(row1));
                            cell3.setText(String.valueOf(row2));
                        }
                    }
                }
            }
        }
        else if (row3 !=0) {
            if (row2 == 0) {
                if (row1 == 0) {
                    if (row0 == 0) {
                        //O
                        //O
                        //O
                        //X
                        //nothing
                    }
                    else if (row0 != 0) {
                        //X
                        //O
                        //O
                        //X
                        if (row0 == row3) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,0);
                            board.setSingleValueOnBoard(3,columnNumber,row0+row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(row0+row3));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0));
                            cell3.setText(String.valueOf(row3));
                        }
                    }
                }
                else if (row1 != 0) {
                    if (row0 == 0) {
                        //O
                        //X
                        //O
                        //X
                        if (row1 == row3) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,0);
                            board.setSingleValueOnBoard(3,columnNumber,row1+row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(row1+row3));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row1);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row1));
                            cell3.setText(String.valueOf(row3));
                        }
                    }
                    else if (row0 != 0) {
                        //X
                        //X
                        //O
                        //X
                        if (row1==row3) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0);
                            board.setSingleValueOnBoard(3,columnNumber,row1+row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0));
                            cell3.setText(String.valueOf(row1+row3));
                        } else if (row0==row1) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0+row1);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0+row1));
                            cell3.setText(String.valueOf(row3));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,row0);
                            board.setSingleValueOnBoard(2,columnNumber,row1);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText(String.valueOf(row0));
                            cell2.setText(String.valueOf(row1));
                            cell3.setText(String.valueOf(row3));
                        }
                    }
                }
            }
            else if (row2 != 0) {
                if (row1 == 0) {
                    if (row0 == 0) {
                        //O
                        //O
                        //X
                        //X
                        if (row2 == row3) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,0);
                            board.setSingleValueOnBoard(3,columnNumber,row2+row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText("");
                            cell3.setText(String.valueOf(row2+row3));
                        } else {
                            //nothing
                        }
                    }
                    else if (row0 != 0) {
                        //X
                        //O
                        //X
                        //X
                        if (row2 == row3) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0);
                            board.setSingleValueOnBoard(3,columnNumber,row2+row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0));
                            cell3.setText(String.valueOf(row2+row3));
                        } else if (row0 == row2) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row0+row2);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row0+row2));
                            cell3.setText(String.valueOf(row3));
                        } else {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,row0);
                            board.setSingleValueOnBoard(2,columnNumber,row2);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText(String.valueOf(row0));
                            cell2.setText(String.valueOf(row2));
                            cell3.setText(String.valueOf(row3));
                        }
                    }
                } else if (row1 != 0) {
                    if (row0 == 0) {
                        //O
                        //X
                        //X
                        //X
                        if (row2 == row3) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row1);
                            board.setSingleValueOnBoard(3,columnNumber,row2+row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row1));
                            cell3.setText(String.valueOf(row2+row3));
                        } else if (row1 == row2) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,0);
                            board.setSingleValueOnBoard(2,columnNumber,row1+row2);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText("");
                            cell2.setText(String.valueOf(row1+row2));
                            cell3.setText(String.valueOf(row3));
                        } else {
                            //nothing
                        }
                    }
                    else if (row0 != 0) {
                        //X
                        //X
                        //X
                        //X
                        if(row2==row3) {
                            if(row0==row1) {
                                board.setSingleValueOnBoard(0,columnNumber,0);
                                board.setSingleValueOnBoard(1,columnNumber,0);
                                board.setSingleValueOnBoard(2,columnNumber,row0+row1);
                                board.setSingleValueOnBoard(3,columnNumber,row2+row3);
                                cell0.setText("");
                                cell1.setText("");
                                cell2.setText(String.valueOf(row0+row1));
                                cell3.setText(String.valueOf(row2+row3));
                            } else {
                                board.setSingleValueOnBoard(0,columnNumber,0);
                                board.setSingleValueOnBoard(1,columnNumber,row0);
                                board.setSingleValueOnBoard(2,columnNumber,row1);
                                board.setSingleValueOnBoard(3,columnNumber,row2+row3);
                                cell0.setText("");
                                cell1.setText(String.valueOf(row0));
                                cell2.setText(String.valueOf(row1));
                                cell3.setText(String.valueOf(row2+row3));
                            }
                        } else if (row1==row2) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,row0);
                            board.setSingleValueOnBoard(2,columnNumber,row1+row2);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText(String.valueOf(row0));
                            cell2.setText(String.valueOf(row1+row2));
                            cell3.setText(String.valueOf(row3));
                        } else if (row0==row1) {
                            board.setSingleValueOnBoard(0,columnNumber,0);
                            board.setSingleValueOnBoard(1,columnNumber,row0+row1);
                            board.setSingleValueOnBoard(2,columnNumber,row2);
                            board.setSingleValueOnBoard(3,columnNumber,row3);
                            cell0.setText("");
                            cell1.setText(String.valueOf(row0+row1));
                            cell2.setText(String.valueOf(row2));
                            cell3.setText(String.valueOf(row3));
                        } else {
                            //nothing
                        }
                    }
                }
            }
        }
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.silde_down);
        cell0.startAnimation(anim);
        cell1.startAnimation(anim);
        cell2.startAnimation(anim);
        cell3.startAnimation(anim);
    }

    private void modifyColumnUp(int columnNumber, TextView[][] cells, Board board) {

    }
    private void newNumber(TextView[][] cells, Board board) {
        int random1 = drawNumber();
        int random2 = drawNumber();
        while (board.getSingleValueFromBoard(random1,random2) != 0) {
            random1 = drawNumber();
            random2 = drawNumber();
        }
        cells[random1][random2].setText("2");

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        cells[random1][random2].startAnimation(anim);

        board.setSingleValueOnBoard(random1,random2,2);
    }
    private int drawNumber() {
        int random = new Random().nextInt(4);
        return random;
    }

}
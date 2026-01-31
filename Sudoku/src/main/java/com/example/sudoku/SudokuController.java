package com.example.sudoku;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//stores game state
public class SudokuController {
    private PuzzleGenerator generator = new PuzzleGenerator();
    private int[][] current;
    private int[][] solution;
    private int[][] original;
    private boolean complete = false;

    //complete and correct
    private void checkComplete() {
        boolean solved = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (current[i][j] != solution[i][j]) {
                    solved = false;
                    break;
                }
            }
            if (solved == false) { break; }
        }
        this.complete = solved;
    }

    //complete but incorrect
    private boolean incorrect() {
        boolean filled = true;
        boolean solved = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (current[i][j] == 0) {
                    filled = false;
                    break;
                }
                if (current[i][j] != solution[i][j]) {
                    solved = false;
                }
            }
            if (filled == false) { break; }
        }
        return filled && !solved;
    }

    //for GET requests at root
    @GetMapping("/")
    public String home(Model model) {
        //starts new game on load
        if (current == null) {
            newGame();
        }
        model.addAttribute("current", current);
        model.addAttribute("original", original);
        model.addAttribute("complete", complete);
        model.addAttribute("incorrect", incorrect());

        return "index";
    }

    @GetMapping("/newGame") 
    public String newGame() {
        //creates new solution and puzzle
        solution = generator.generateSolved();
        current = generator.createPuzzle(solution);
        original = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                original[i][j] = current[i][j];
            }
        }
    complete = false;
    return "redirect:/"; //redirects to root
    }

    //for POST requests to /makemove
    @PostMapping("/makemove")
    public String makemove(@RequestParam("row") int row,
                           @RequestParam("col") int col,
                           @RequestParam("value") int value) {

        if (original[row][col] == 0) { //if empty in original board
            current[row][col] = value;
        }
        checkComplete();
        return "redirect:/";
    }
}


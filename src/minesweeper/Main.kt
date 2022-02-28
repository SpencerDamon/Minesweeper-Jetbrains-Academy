package minesweeper

import kotlin.random.Random

const val ROWS = 9
const val COLUMNS = 9

// Lay the 9 x 9 field with safe cells
class MineField(var numOfMines: Int = 10, var rows: Int = ROWS, var columns: Int = COLUMNS) {

    val mine = "X"
    val safeCell = "."
    val mineCell = -1
    val exploredCell = -2
    val freeCell = -3
    val board = MutableList(columns) { MutableList(rows) { safeCell } }
    val hiddenBoard: MutableList<MutableList<Int>> = MutableList(columns) { MutableList(rows) { freeCell } }

    // Lay the mines, and check if a mine is already there.
    fun layMines() {
        var mineCount = numOfMines
        while (mineCount > 0) {
            val x = Random.nextInt(0, rows)
            val y = Random.nextInt(0, columns)

            if (hiddenBoard[x][y] != mineCell) {
                hiddenBoard[x][y] = mineCell
                mineCount--
            }
        }
    }

    // Returns false if out of bounds
    fun isValid(board: MutableList<MutableList<Int>>, x: Int, y: Int): Boolean {
        return when {
            // check if beyond the row size of the minefield or a negative row number which would be out of bounds
            x <= - 1 || x >= board.size -> false
            // check column
            y <= - 1 || y >= board[x].size -> false
            else -> true
        }
    }

    // check for neighboring mines in minefield
    fun isMineAddHint() {
        for (x in 0 until ROWS) {
            for (y in 0 until COLUMNS) {
                var counter = 0
                val currentCell = hiddenBoard[x][y] // val here - resets every iteration

                if (isValid(hiddenBoard, x - 1, y - 1)) {
                    if (hiddenBoard[x - 1][y - 1] == mineCell) counter++
                }
                if (isValid(hiddenBoard, x - 1, y + 1)) {
                    if (hiddenBoard[x - 1][y + 1] == mineCell) counter++
                }
                if (isValid(hiddenBoard, x - 1, y)) {
                    if (hiddenBoard[x - 1][y] == mineCell) counter++
                }
                if (isValid(hiddenBoard, x + 1, y - 1)) {
                    if (hiddenBoard[x + 1][y - 1] == mineCell) counter++
                }
                if (isValid(hiddenBoard, x + 1, y + 1)) {
                    if (hiddenBoard[x + 1][y + 1] == mineCell) counter++
                }
                if (isValid(hiddenBoard, x + 1, y)) {
                    if (hiddenBoard[x + 1][y] == mineCell) counter++
                }
                if (isValid(hiddenBoard, x, y - 1)) {
                    if (hiddenBoard[x][y - 1] == mineCell) counter++
                }
                if (isValid(hiddenBoard, x, y + 1)) {
                    if (hiddenBoard[x][y + 1] == mineCell) counter++
                }

                if (currentCell != mineCell) {
                    if (counter != 0) {
                        hiddenBoard[x][y] = counter
                    }
                }
            }
        }
    }

    fun drawPlayerField() {
        print(" |")
        for (x in 0 until board.size) {
            print(x + 1)
        }
        println("|")
        // Draws "-|-----|"  to size of field
        print("-|")
        for (x in 0 until board.size) {
            print("-")
        }
        println("|")
        // print player view board
        for (x in 0 until board.size) {
            val playerBoard = board[x].joinToString("")
            print("${x + 1}|")
            print(playerBoard)
            println("|")
        }
        // Draws "-|-----|"  to size of field
        print("-|")
        for (x in 0 until board.size) {
            print("-")
        }
        println("|")
    }
    var mineCountDown = 0
    val totalExplorableCells = rows * columns - numOfMines
    var cellCount = 0
    var playerLost = false
    var playerWins = false
    var gameOn = true
    var firstMoveFree = true

    fun showMines() {
        for (row in 0 until hiddenBoard.size) {
            for (column in 0 until hiddenBoard[row].size) {
                if (hiddenBoard[row][column] == mineCell) board[row][column] = mine
            }
        }
    }

    // Not happy with this BFD
    fun addNumberCells(): Int {
        var count = 0
        for (i in board.indices) {
            for (j in board.indices) {
                if (board[i][j] == "1") count++
                if (board[i][j] == "2") count++
                if (board[i][j] == "3") count++
                if (board[i][j] == "4") count++
                if (board[i][j] == "5") count++
                if (board[i][j] == "6") count++
                if (board[i][j] == "7") count++
                if (board[i][j] == "8") count++
            }
        }
        return count
    }

    fun recursion(x: Int, y: Int, prevState: Int, newState: Int): Int {
        // base case if off board or already explored do nothing
        if (!isValid(hiddenBoard, x, y) || hiddenBoard[x][y] != prevState) return 0
        // replace state from -3 (safeCell) -> -2 (explored cell)
        hiddenBoard[x][y] = newState
        board[x][y] = "/"

        // add to total count of explored cells
        cellCount++

        // Check neighboring cells of current recursive cell, if num change the player board cell to num, count cell
        if (isValid(hiddenBoard, x - 1, y - 1)) {
            if (hiddenBoard[x - 1][y - 1] > 0) board[x - 1][y - 1] = "${hiddenBoard[x - 1][y - 1]}"
        }
        if (isValid(hiddenBoard, x - 1, y + 1)) {
            if (hiddenBoard[x - 1][y + 1] > 0) board[x - 1][y + 1] = "${hiddenBoard[x - 1][y + 1]}"
        }
        if (isValid(hiddenBoard, x - 1, y)) {
            if (hiddenBoard[x - 1][y] > 0) board[x - 1][y] = "${hiddenBoard[x - 1][y]}"
        }
        if (isValid(hiddenBoard, x + 1, y - 1)) {
            if (hiddenBoard[x + 1][y - 1] > 0) board[x + 1][y - 1] = "${hiddenBoard[x + 1][y - 1]}"
        }
        if (isValid(hiddenBoard, x + 1, y + 1)) {
            if (hiddenBoard[x + 1][y + 1] > 0) board[x + 1][y + 1] = "${hiddenBoard[x + 1][y + 1]}"
        }
        if (isValid(hiddenBoard, x + 1, y)) {
            if (hiddenBoard[x + 1][y] > 0) board[x + 1][y] = "${hiddenBoard[x + 1][y]}"
        }
        if (isValid(hiddenBoard, x, y - 1)) {
            if (hiddenBoard[x][y - 1] > 0) board[x][y - 1] = "${hiddenBoard[x][y - 1]}"
        }
        if (isValid(hiddenBoard, x, y + 1)) {
            if (hiddenBoard[x][y + 1] > 0) board[x][y + 1] = "${hiddenBoard[x][y + 1]}"
        }
        // recursive call for 8 dir
        recursion(x - 1, y - 1, prevState, newState) // northwest
        recursion(x - 1, y, prevState, newState) // north
        recursion(x - 1, y + 1, prevState, newState) // northeast
        recursion(x, y + 1, prevState, newState) // east
        recursion(x + 1, y + 1, prevState, newState) // southeast
        recursion(x + 1, y, prevState, newState) // south
        recursion(x + 1, y - 1, prevState, newState) // southwest
        recursion(x, y - 1, prevState, newState) // west
        return cellCount
    }

    fun markUnmarkMine(x: Int, y: Int) {
        when (board[x][y]) {
            "." -> board[x][y] = "*"
            "*" -> board[x][y] = "."
        }
    }
    fun countMines(x: Int, y: Int) {
        if (board[x][y] == "*" && hiddenBoard[x][y] == mineCell) mineCountDown++
        if (board[x][y] == "." && hiddenBoard[x][y] == mineCell) mineCountDown--
    }

    companion object {
        fun playGame(mineField: MineField) {

            while (mineField.gameOn) {
                println("Set/delete mines marks or claim a cell as free:")
                val (xCoor, yCoor, command) = readln().split(" ")
                val x = yCoor.toInt() - 1
                val y = xCoor.toInt() - 1

                // marks mines <-> safeCells, counts marked mines, draws string field
                if (command == "mine" && mineField.isValid(mineField.hiddenBoard, x, y)) {
                    mineField.markUnmarkMine(x, y)
                    mineField.countMines(x, y)
                    if (mineField.mineCountDown == mineField.numOfMines) {
                        mineField.playerWins = true
                        mineField.gameOn = false
                    }
                }
                if (command == "free" && mineField.isValid(mineField.hiddenBoard, x, y)) {

                    when (mineField.hiddenBoard[x][y]) {
                        mineField.mineCell -> {
                            mineField.playerLost = true // stepped on mine!
                            mineField.gameOn = false
                            mineField.showMines()
                        }
                        in 1..mineField.board.size -> {
                            mineField.board[x][y] = "${mineField.hiddenBoard[x][y]}"
                            mineField.firstMoveFree = false
                            val temp1 = mineField.cellCount + mineField.addNumberCells()
                            if (temp1 == mineField.totalExplorableCells) {
                                mineField.playerWins = true
                                mineField.gameOn = false
                            }
                        }
                        mineField.freeCell -> {
                            mineField.recursion(x, y, mineField.freeCell, mineField.exploredCell)
                            val temp2 = mineField.cellCount + mineField.addNumberCells()
                            mineField.firstMoveFree = false
                            if (temp2 == mineField.totalExplorableCells) {
                                mineField.playerWins = true
                                mineField.gameOn = false
                            }
                        }
                    }
                }
                mineField.drawPlayerField()
            }
            if (mineField.playerWins) {
                println("Congratulations! You found all the mines!")
            }
            if (mineField.playerLost) println("You stepped on a mine and failed!")
        }
    }
}

fun main() {
    val mineFieldOne = MineField(numberOfMines())
    mineFieldOne.layMines()
    mineFieldOne.isMineAddHint()
    mineFieldOne.drawPlayerField()
    MineField.playGame(mineFieldOne)
}

fun numberOfMines(): Int {
    println("How many mines do you want on the field?")
    return readLine()!!.toInt()
}

import React, { useState, useEffect } from 'react';
import '../Tictactoe.css';

const TictacToe = () => {
  const [board, setBoard] = useState(Array(9).fill(null));
  const [isXNext, setIsXNext] = useState(true);
  const [winner, setWinner] = useState(null);
  const [isDraw, setIsDraw] = useState(false); // Thêm state để kiểm tra kết quả hòa
  const [socket, setSocket] = useState(null);

  useEffect(() => {
    const newSocket = new WebSocket('ws://localhost:8080/tictactoe');
    newSocket.onmessage = (event) => {
      const { board, isXNext } = JSON.parse(event.data);
      setBoard(board);
      setIsXNext(isXNext);
      const newWinner = calculateWinner(board);
      if (newWinner) {
        setWinner(newWinner);
      } else if (isBoardFull(board)) {
        setIsDraw(true); // Kiểm tra kết quả hòa
      }
    };
    setSocket(newSocket);
    return () => newSocket.close();
  }, []);

  const handleClick = (index) => {
    if (board[index] || winner || isDraw) return; // Thêm kiểm tra isDraw

    const newBoard = board.slice();
    newBoard[index] = isXNext ? 'X' : 'O';
    const newWinner = calculateWinner(newBoard);
    
    setBoard(newBoard);
    setIsXNext(!isXNext);
    if (newWinner) {
      setWinner(newWinner);
    } else if (isBoardFull(newBoard)) {
      setIsDraw(true); // Kiểm tra kết quả hòa
    }

    socket.send(JSON.stringify({ board: newBoard, isXNext: !isXNext }));
  };

  const handlePlayAgain = () => {
    setBoard(Array(9).fill(null));
    setIsXNext(true);
    setWinner(null);
    setIsDraw(false); // Thiết lập lại trạng thái hòa
    socket.send(JSON.stringify({ board: Array(9).fill(null), isXNext: true }));
  };

  const calculateWinner = (board) => {
    const lines = [
      [0, 1, 2],
      [3, 4, 5],
      [6, 7, 8],
      [0, 3, 6],
      [1, 4, 7],
      [2, 5, 8],
      [0, 4, 8],
      [2, 4, 6],
    ];
    for (let i = 0; i < lines.length; i++) {
      const [a, b, c] = lines[i];
      if (board[a] && board[a] === board[b] && board[a] === board[c]) {
        return board[a];
      }
    }
    return null;
  };

  const isBoardFull = (board) => {
    return board.every(cell => cell !== null);
  };

  const renderSquare = (index) => {
    return (
      <button className="square" onClick={() => handleClick(index)}>
        {board[index]}
      </button>
    );
  };

  const renderStatus = () => {
    if (winner) {
      return `Winner: ${winner}`;
    } else if (isDraw) {
      return 'Draw!'; // Hiển thị kết quả hòa
    } else {
      return `Next player: ${isXNext ? 'X' : 'O'}`;
    }
  };

  return (
    <div className="game">
      <header className="header">Tic Tac Toe</header>
      <div className="game-board">
        <div className="board-row">
          {renderSquare(0)}
          {renderSquare(1)}
          {renderSquare(2)}
        </div>
        <div className="board-row">
          {renderSquare(3)}
          {renderSquare(4)}
          {renderSquare(5)}
        </div>
        <div className="board-row">
          {renderSquare(6)}
          {renderSquare(7)}
          {renderSquare(8)}
        </div>
      </div>
      <div className="game-info">
        <div className="status">{renderStatus()}</div>
        {(winner || isDraw) && (
          <button className="play-again" onClick={handlePlayAgain}>
            Play Again
          </button>
        )}
      </div>
    </div>
  );
};

export default TictacToe;

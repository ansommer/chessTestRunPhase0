package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

//    @Override
//    public String toString() {
//        return "make a tostring";
//    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>(); //honestly idk the difference btwn collection, hashset, and array
        if (this.type == ChessPiece.PieceType.KING) {
            moves = kingMoves(board, myPosition);
        } else if  (this.type == ChessPiece.PieceType.KNIGHT) {
            moves = knightMoves(board, myPosition);
        } else if (this.type == ChessPiece.PieceType.BISHOP) {
            moves = bishopMoves(board, myPosition);
        } else if (this.type == ChessPiece.PieceType.ROOK) {
            moves = rookMoves(board, myPosition);
        } else if (this.type == ChessPiece.PieceType.QUEEN) {
            moves = rookMoves(board, myPosition);
            Collection<ChessMove> moves2 = new HashSet<>();
            moves2 = bishopMoves(board, myPosition);
            moves.addAll(moves2);
        } else if (this.type == ChessPiece.PieceType.PAWN) {
            moves = pawnMoves(board, myPosition);
        }
        return moves;
    }

    public boolean checkMove (ChessBoard board, ChessPosition myPosition, int row, int col) {
        if (1 <= row && row <= 8 && 1 <= col && col <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece == null || piece.pieceColor != this.pieceColor) {
                return true;
            }
        }
        return false;
    }

    public HashSet<ChessMove> pawnPromo(ChessBoard board, ChessPosition myPosition, int row, int col, HashSet<ChessMove> moves) {
        moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.ROOK));
        moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.KNIGHT));

        return moves;
    }

    public HashSet<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        HashSet<ChessMove> promomoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (this.pieceColor == ChessGame.TeamColor.BLACK) {
            //check if can move forward 1
            ChessPiece piece = board.getPiece(new ChessPosition(row-1, col));
            if (piece == null) {
                if (row-1 == 1) {
                    promomoves = pawnPromo(board, myPosition, row-1, col, moves);
                    moves.addAll(promomoves);
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col), null));
                    if (row == 7) {
                        piece = board.getPiece(new ChessPosition(row-2, col));
                        if (piece == null) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row-2, col), null));
                        }
                    }
                }
            }
            //check if can move forward 2

            //check if can move diagonally
            if (1 <= row - 1 && row - 1 <= 8 && 1 <= col + 1 && col + 1 <= 8) {
            piece =     board.getPiece(new ChessPosition(row-1, col+1));
                if (piece != null) {
                    if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
                        if (row - 1 == 1) {
                            promomoves = pawnPromo(board, myPosition, row - 1, col + 1, moves);
                            moves.addAll(promomoves);
                        } else if (1 <= row - 1 && row - 1 <= 8 && 1 <= col + 1 && col + 1 <= 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), null));
                        }
                    }
                }
            }
            if (1 <= row-1 && row-1 <= 8 && 1 <= col-1 && col-1 <= 8) {
                piece = board.getPiece(new ChessPosition(row-1, col-1));
                if (piece != null) {
                    if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
                        if (row - 1 == 1) {
                            promomoves = pawnPromo(board, myPosition, row - 1, col - 1, moves);
                            moves.addAll(promomoves);
                        } else if (1 <= row - 1 && row - 1 <= 8 && 1 <= col - 1 && col - 1 <= 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), null));
                        }
                    }
                }
            }
        }

        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            ChessPiece piece = board.getPiece(new ChessPosition(row+1, col));
            if (piece == null) {
                if (row+1 == 8) {
                    promomoves = pawnPromo(board, myPosition, row+1, col, moves);
                    moves.addAll(promomoves);
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col), null));
                    //check if can move forward 2
                    if (row == 2) {
                        piece = board.getPiece(new ChessPosition(row+2, col));
                        if (piece == null) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row+2, col), null));
                        }
                    }
                }
            }

            //check if can move diagonally
            if (1 <= row+1 && row+1 <= 8 && 1 <= col+1 && col+1 <= 8) {
                piece = board.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null) {
                    if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
                        if (row + 1 == 8) {
                            promomoves = pawnPromo(board, myPosition, row + 1, col + 1, moves);
                            moves.addAll(promomoves);
                        } else if (1 <= row + 1 && row + 1 <= 8 && 1 <= col + 1 && col + 1 <= 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), null));
                        }
                    }
                }
            }
            if (1 <= row+1 && row+1 <= 8 && 1 <= col-1 && col-1 <= 8) {
                piece = board.getPiece(new ChessPosition(row+1, col-1));
                if (piece != null) {
                    if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
                        if (row + 1 == 8) {
                            promomoves = pawnPromo(board, myPosition, row + 1, col - 1, moves);
                            moves.addAll(promomoves);
                        } else if (1 <= row + 1 && row + 1 <= 8 && 1 <= col - 1 && col - 1 <= 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), null));
                        }
                    }
                }
            }
        }

        return moves;
    }

    public HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        while(row<8) {
            row++;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }
        row = myPosition.getRow();

        while(row>1) {
            row--;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }
        row = myPosition.getRow();

        while(col<8) {
            col++;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }
        col = myPosition.getColumn();

        while(col>1) {
            col--;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }

        return moves;
    }

    public HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        while(row<8 && col<8) {
            row++;
            col++;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();

        while(row>1 && col>1) {
            row--;
            col--;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();

        while(row>1 && col<8) {
            row--;
            col++;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();

        while(row<8 && col>1) {
            row++;
            col--;
            if (checkMove(board, myPosition, row, col)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {break;}
            } else {break;}
        }

        return moves;
    }

    public HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        HashSet<ChessPosition> possibleMoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        possibleMoves.add(new ChessPosition(row+1, col-1));
        possibleMoves.add(new ChessPosition(row+1, col));
        possibleMoves.add(new ChessPosition(row+1, col+1));
        possibleMoves.add(new ChessPosition(row, col-1));
        possibleMoves.add(new ChessPosition(row, col+1));
        possibleMoves.add(new ChessPosition(row-1, col-1));
        possibleMoves.add(new ChessPosition(row-1, col));
        possibleMoves.add(new ChessPosition(row-1, col+1));

        for (ChessPosition x : possibleMoves) {
            int testRow = x.getRow();
            int testCol = x.getColumn();
            if (checkMove(board, x, testRow, testCol)) {
                moves.add(new ChessMove(myPosition, x, null));
            }
        }

        return moves;
    }

    public HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        HashSet<ChessPosition> possibleMoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        possibleMoves.add(new ChessPosition(row+2, col+1));
        possibleMoves.add(new ChessPosition(row+2, col-1));
        possibleMoves.add(new ChessPosition(row-2, col+1));
        possibleMoves.add(new ChessPosition(row-2, col-1));
        possibleMoves.add(new ChessPosition(row+1, col+2));
        possibleMoves.add(new ChessPosition(row-1, col+2));
        possibleMoves.add(new ChessPosition(row+1, col-2));
        possibleMoves.add(new ChessPosition(row-1, col-2));


        for (ChessPosition x : possibleMoves) {
            int testRow = x.getRow();
            int testCol = x.getColumn();
            if (checkMove(board, x, testRow, testCol)) {
                moves.add(new ChessMove(myPosition, x, null));
            }
        }
        return moves;

    }


}

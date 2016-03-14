package bot;

import java.util.ArrayList;

import bot.strategies.MinimaxCostin;

public class BotStarter {

	public static final boolean DEV_MODE = true;

	/**
	 * Makes a turn. Edit this method to make your bot smarter. Currently does
	 * only random moves.
	 * 
	 * @return The column where the turn was made.
	 */
	public Move makeTurn(FieldImplementation field) {
		int depth = (field.getRoundNumber() > 25) ? 5 : 3;
		// int depth = 4;
		MinimaxCostin strategyChildren = new MinimaxCostin(1);
		Move fatalistMove = strategyChildren.nextMove(field);
		if (strategyChildren.root != null && strategyChildren.root.children != null) {			
		for (AbstractMinimax.Node n : strategyChildren.root.children) {
			if (n.score == strategyChildren.root.score
					&& n.field.bigBoardWinner() == FieldInterface.Winner.MYSELF) {
				System.err.println("Fatalist move");
				if (fatalistMove != null)				
					return fatalistMove;
			}
		}
		}
		MinimaxCostin strategy = new MinimaxCostin(depth);
		Move move = strategy.nextMove(field);
		return move;
	}

	public static void main(String[] args) {
		BotParser parser = new BotParser(new BotStarter());
		parser.run();
	}
}

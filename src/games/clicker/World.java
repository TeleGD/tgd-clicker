package games.clicker;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppFont;
import app.AppGame;
import app.AppInput;
import app.AppLoader;
import app.AppPlayer;
import app.AppWorld;

public class World extends AppWorld {

	public static final Font Font = AppLoader.loadFont("/fonts/vt323.ttf", AppFont.BOLD, 18);
	public final static String DIRECTORY_MUSICS="/musics/clicker/";
	private ArrayList<Player> players;
	private int count;
	private boolean finished;
	private int height;
	private int width;
	private int nbJoueursInit;
	private static int marge = 10;

	private static Audio music;
	private float musicPos;

	static {
		music = AppLoader.loadAudio(DIRECTORY_MUSICS+"Sticky_and_Addictive.ogg");
	}

	public World (int ID) {
		super (ID);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		if (finished) {
			context.setFont(Font);
			for (int i = players.size() - 1; i >= 0; i--) {
				context.setColor(players.get(i).getColor());
				String s = players.get(i).getName() + " : " + players.get(i).getScore();
				context.drawString(s, width / 2 - Font.getWidth(s) / 2,
						height / 2 - (Font.getHeight(s) + 5) * (nbJoueursInit / 2 - i));
			}
		} else {
			if (nbJoueursInit > 2) {
				context.setColor(Color.white);
				context.fillRect(0, 0, width, height);
				context.setColor(Color.black);
				context.fillRect(marge, marge, width / 2 - 2 * marge, height / 2 - 2 * marge);
				context.fillRect(marge + width / 2, marge, width / 2 - 2 * marge, height / 2 - 2 * marge);
				context.fillRect(marge, marge + height / 2, width / 2 - 2 * marge, height / 2 - 2 * marge);
				context.fillRect(marge + width / 2, marge + height / 2, width / 2 - 2 * marge, height / 2 - 2 * marge);
				context.setColor(Color.white);
				context.drawRect(width / 2 - width / 6, height / 2 - height / 6, width / 3, height / 3);
				context.setColor(Color.black);
				context.fillRect(width / 2 - width / 6 + 1, height / 2 - height / 6 + 1, width / 3 - 1, height / 3 - 1);
				if (nbJoueursInit == 3) {
					Image image = AppLoader.loadPicture("/images/clicker/lucas.png");
					image.draw(width * 3 / 4 - 8 * marge, height * 3 / 4 - 13 * marge);
				}
				for (int i = 0; i < nbJoueursInit; i++) {
					context.setColor(players.get(i).getColor());
					String s = players.get(i).getName() + " : " + players.get(i).getScore();
					int x = (width + 2 * marge) / 4 - Font.getWidth(s) / 2;
					int y = (height + 2 * marge) / 4 - Font.getHeight(s) / 2;
					context.drawString(s, (i & 1) * width / 2 + x, (i & 2) * height / 2 + y);
				}
			} else {
				context.setColor(Color.white);
				context.fillRect(0, 0, width, height);
				context.setColor(Color.black);
				context.fillRect(marge, marge, width / 2 - 2 * marge, height - 2 * marge);
				context.fillRect(marge + width / 2, marge, width / 2 - 2 * marge, height - 2 * marge);
				context.setColor(Color.white);
				context.drawRect(width / 2 - width / 6, height / 2 - height / 6, width / 3, height / 3);
				context.setColor(Color.black);
				context.fillRect(width / 2 - width / 6 + 1, height / 2 - height / 6 + 1, width / 3 - 1, height / 3 - 1);
				for (int i = 0; i < nbJoueursInit; i++) {
					context.setColor(players.get(i).getColor());
					String s = players.get(i).getName() + " : " + players.get(i).getScore();
					int x = (width + 2 * marge) / 6 - Font.getWidth(s) / 2;
					int y = (height + 2 * marge) / 2 - Font.getHeight(s) / 2;
					context.drawString(s, i * 4 * width / 6 + x, y);
				}
			}
			context.setColor(Color.white);
			String s2 = "Temps restant : " + count / 1000;
			context.drawString(s2, width / 2 - 75, height / 2);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		super.update (container, game, delta);
		count -= delta;
		for (Player player : players) {
			player.update(container, game, delta);
		}
		if (count < 0) {
			finished = true;
			music.stop();
		}
	}

	@Override
	public void play(GameContainer container, StateBasedGame game) {
		AppGame appGame = (AppGame) game;
		AppInput appInput = (AppInput) container.getInput ();
		appInput.clearKeyPressedRecord ();
		appInput.clearControlPressedRecord ();
		finished = false;
		music.playAsMusic(1f, .4f, true);
		count = 30000;
		players = new ArrayList<Player>();
		for (AppPlayer player : appGame.appPlayers) {
			players.add(new Player(player));
		}
		height = container.getHeight();
		width = container.getWidth();
		nbJoueursInit = appGame.appPlayers.size();
	}

	@Override
	public void pause(GameContainer container, StateBasedGame game) {
		this.musicPos = music.getPosition();
		music.stop();
	}

	@Override
	public void resume(GameContainer container, StateBasedGame game) {
		music.playAsMusic(1, 4f, true);
		music.setPosition(this.musicPos);
	}

	@Override
	public void stop(GameContainer container, StateBasedGame game) {
		music.stop();
	}

}

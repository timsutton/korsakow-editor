package org.korsakow.ide.ui.components;

/**
 * 
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.korsakow.ide.Application;
import org.korsakow.ide.exception.MediaRuntimeException;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.resources.media.PlayableImage;
import org.korsakow.ide.resources.media.PlayableVideo;
import org.korsakow.ide.resources.media.UnsupportedVideo;
import org.korsakow.ide.ui.components.layout.VerticalFlowLayout;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;

/**
 * Replaces MediaPanel. Rename classes after the swap.
 * @author d
 *
 */
public class NewMediaPanel extends JPanel //  extends ResourcePanel<M>
{
	private static final int SLIDER_MAX = 100*1000;
	private static final String TIME_FORMAT_REGEX = "(?:(\\d+):)?(\\d{1,2})(?:[.](\\d{1,3}))?";
	public static String formatTime(long millis) {
		long ms = millis%100;
		long s = (long)(Math.floor(millis/1000.0)%60);
		long m = (long)(Math.floor(millis/1000.0/60.0));
		return String.format("%02d:%02d.%02d", m, s, ms);
	}
	/**
	 * @see TIME_FORMAT_REGEX
	 * @return null if improperly formatted
	 */
	public static Long parseTime(String time) {
		if (!time.matches(TIME_FORMAT_REGEX))
			return null;
		long m;
		long s;
		long ms;
		try { m  = Long.parseLong(time.replaceAll(TIME_FORMAT_REGEX, "$1")); } catch (NumberFormatException e) { m = 0; }
		try { ms = Long.parseLong(time.replaceAll(TIME_FORMAT_REGEX, "$3")); } catch (NumberFormatException e) { ms = 0; }
		try { 
			s  = Long.parseLong(time.replaceAll(TIME_FORMAT_REGEX, "$2"));
		} catch (NumberFormatException e) { 
			return null;
		}
		return m*1000*60 + s*1000 + ms;
	}
	protected JLabel loadingLabel;
	protected Playable playable;
	protected Component mediaComponent;
	protected JPanel mediaPlaceHolder;
	protected JButton playButton;
	protected JButton stopButton;
	protected JButton seekStartButton;
	protected JButton seekEndButton;
	protected JSlider seekSlider;
	protected JPanel buttonPanel;
	protected JLabel timeLabel;
	protected Timer seekSliderTimer;
	protected boolean seekSliderDontUpdate = false;
	protected boolean isTemporal = false;
	
	private long duration;
	public NewMediaPanel()
	{
		initUI();
		initListeners();
	}
	protected void initUI()
	{
		setLayout(new BorderLayout());
		
		mediaPlaceHolder = new JPanel(null);
		mediaPlaceHolder.setBackground(Color.black);
		add(mediaPlaceHolder, BorderLayout.CENTER);
		
		loadingLabel = new JLabel(UIResourceManager.getIcon("Throbber_allbackgrounds_circledots_32.gif"));
		loadingLabel.setBounds(0, 0, 32, 32);
		mediaPlaceHolder.add(loadingLabel);
		
		seekSlider = new JSlider(JSlider.HORIZONTAL, 0, SLIDER_MAX, 0);
		seekSlider.setPaintLabels(false);
		seekSlider.setPaintTicks(false);
		seekSlider.setPaintTrack(true);
		
		timeLabel = new JLabel();
		timeLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		timeLabel.setText(formatTime(0));

//		mediaPanel.add(seekSlider);
		buttonPanel = new JPanel(new BorderLayout(0, 0));
		add(buttonPanel, BorderLayout.SOUTH);
		seekStartButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_CONTROL_SEEK_START));
//		buttonPanel.add(seekStartButton);
		JPanel playPanel = new JPanel(new CardLayout());
		buttonPanel.add(playPanel, BorderLayout.WEST);
		playButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_CONTROL_PLAY));
		playButton.setBorderPainted(false);
		playButton.setContentAreaFilled(false);
		playPanel.add(playButton, "play");
		stopButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_CONTROL_STOP));
		stopButton.setBorderPainted(false);
		stopButton.setContentAreaFilled(false);
		playPanel.add(stopButton, "stop");
		stopButton.setVisible(false);
		seekEndButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_CONTROL_SEEK_END));
//		buttonPanel.add(seekEndButton);
		VerticalFlowLayout flowLayout = new VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 0);
		flowLayout.setMaximizeOtherDimension(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(seekSlider);
		panel.add(timeLabel);
		buttonPanel.add(panel, BorderLayout.CENTER);
		playPanel.setPreferredSize(new Dimension(playButton.getIcon().getIconWidth(), playButton.getIcon().getIconHeight()));
		playPanel.setMaximumSize(new Dimension(playButton.getIcon().getIconWidth(), playButton.getIcon().getIconHeight()));
	}
	protected void initListeners()
	{
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playable.start();
				playButton.setVisible(false);
				stopButton.setVisible(true);
//				seekSliderTimer.start();
			}
		});
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playable.stop();
				playButton.setVisible(true);
				stopButton.setVisible(false);
//				seekSliderTimer.stop();
			}
		});
		seekStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playable.setTime(0);
			}
		});
		seekEndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playable.setTime( duration );
			}
		});
		seekSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (playable == null)
					return;
				final long time = (long)(seekSlider.getValue()*duration/(double)SLIDER_MAX);
				timeLabel.setText(formatTime(time));
				if (seekSliderDontUpdate)
					return;
				playable.setTime(time);
			}
		});
		seekSliderTimer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSliderTimer();
			}
		});
	}
	public void addSeekSliderChangeListener(ChangeListener listener)
	{
		seekSlider.addChangeListener(listener);
	}
	public void setControlsEnabled(boolean enabled)
	{
		playButton.setEnabled(enabled);
		stopButton.setEnabled(enabled);
		seekStartButton.setEnabled(enabled);
		seekEndButton.setEnabled(enabled);
		seekSlider.setEnabled(enabled);
	}
	public void setTemporalControlsVisible(boolean enabled)
	{
		playButton.setVisible(enabled);
		stopButton.setVisible(false);
		seekStartButton.setVisible(enabled);
		seekEndButton.setVisible(enabled);
		seekSlider.setVisible(enabled);
	}
	public void setPlayable(Playable newPlayable)
	{
		isTemporal = false;
		setControlsEnabled(false);
		setMediaComponent(null); // DO THIS BEFORE DISPOSING THE PLAYABLE OR IT WONT GC RIGHT
		seekSliderTimer.stop();
		setTemporalControlsVisible(false);

		if (playable != null) {
			playable.dispose();
			playable = null;
		}
		
		if (newPlayable != null) {
			playable = newPlayable;
			playable.setTime(0); // not sure why this is necessary sometimes
			Component component;
			try {
				component = playable.getComponent();
			} catch (MediaRuntimeException e) {
				component = new UnsupportedVideo("").getComponent();
			}
			setMediaComponent(component);
			setControlsEnabled(true);
			isTemporal = playable.isTemporal();
			setTemporalControlsVisible(isTemporal);
			duration = playable.getDuration();
		} else {
		}
		seekSlider.setValue(0);
//		if (isTemporal)
		if (!seekSliderTimer.isRunning())
			seekSliderTimer.start();
//		else
//			seekSliderTimer.stop();
	}
	protected void setMediaComponent(final Component mediaComponent)
	{
		if (this.mediaComponent != null) {
			this.mediaComponent.getParent().remove(this.mediaComponent);
		}
		this.mediaComponent = mediaComponent;
		if (this.mediaComponent != null) {
			mediaPlaceHolder.add(this.mediaComponent);
		}
		if (mediaComponent != null && playable != null) {
			// runLater: blarg, we have to wait for mediaPlaceHolder to have been layed out so its size is calculated. this is a lazy and not-the-best-way of doing it
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					try {
						Dimension maxSize = mediaPlaceHolder.getSize();
						Dimension size = mediaComponent.getSize();
						if (playable instanceof PlayableVideo) {
							size = ((PlayableVideo)playable).getAspectRespectingDimension(mediaPlaceHolder.getSize());
						} else
						if (playable instanceof PlayableImage) {
							size = ((PlayableImage)playable).getAspectRespectingDimension(mediaPlaceHolder.getSize());
						}
						mediaComponent.setBounds((maxSize.width-size.width)/2, (maxSize.height-size.height)/2, size.width, size.height);
						loadingLabel.setBounds(mediaComponent.getBounds());
					} catch (MediaRuntimeException e) {
						Application.getInstance().showHandledErrorDialog("general.errors.unsupportedplaybackformat.title", "general.errors.unsupportedplaybackformat.message");
					}
				}
			});
		}
		
		repaint();
		revalidate();
	}
	private Component getMediaComponent()
	{
		return mediaComponent;
	}
	private void setMediaTime(long time)
	{
		if (!isTemporal)
			throw new IllegalArgumentException("cant set time of non-temporal media");
		playable.setTime(time);
		seekSliderDontUpdate = true;
		long duration = playable.getDuration();
		long value = SLIDER_MAX*playable.getTime() / (duration!=0?duration:1);
		seekSlider.setValue((int)value);
		seekSliderDontUpdate = false;
	}
	private long getMediaTime()
	{
		if (!isTemporal)
			throw new IllegalArgumentException("cant get time from non-temporal media");
		return playable.getTime();
	}
	private void onSliderTimer()
	{
		if (playable == null)
			return;
		seekSliderDontUpdate = true;
		long value = (long)Math.ceil(SLIDER_MAX*playable.getTime() / ((double)(duration!=0?duration:1)));
		seekSlider.setValue((int)value);
		seekSliderDontUpdate = false;
	}
	private JSlider getSeekSlider()
	{
		return seekSlider;
	}
	public Playable getPlayable()
	{
		return playable;
	}
	public void setDuration( long duration ) {
		this.duration = duration;
	}
	public void dispose()
	{
		setPlayable(null);
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		UIUtil.setChildrenEnabled(this, enabled);
		loadingLabel.setVisible(!enabled);
	}
	
	@Override
	public void doLayout()
	{
		super.doLayout();
		loadingLabel.setBounds(getWidth()/2 - 32/3, getHeight()/2 - 32/2, 32, 32);
	}
}

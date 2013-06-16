package org.korsakow.ide.resources.media;

import javax.sound.sampled.AudioInputStream;

import org.apache.log4j.Logger;

/**
 * Grabbed from: http://www.javafaq.nu/java-example-code-716.html
 * 
 * 
 * Created by IntelliJ IDEA.
 * User: Jonathan Simon
 * Date: Mar 6, 2005
 * Time: 8:48:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AudioInfo {
    private static final int NUM_BITS_PER_BYTE = 8;

    private AudioInputStream audioInputStream;
    private int[][] samplesContainer;

    //cached values
    protected int sampleMax = 0;
    protected int sampleMin = 0;
    protected double biggestSample;

    public AudioInfo(AudioInputStream aiStream) {
        this.audioInputStream = aiStream;
        createSampleArrayCollection();
    }

    public int getNumberOfChannels(){
        int numBytesPerSample = audioInputStream.getFormat().getSampleSizeInBits() / NUM_BITS_PER_BYTE;
        return audioInputStream.getFormat().getFrameSize() / numBytesPerSample;
    }

    private void createSampleArrayCollection() {
        try {
            audioInputStream.mark(Integer.MAX_VALUE);
            audioInputStream.reset();
            byte[] bytes = new byte[(int) (audioInputStream.getFrameLength()) * ((int) audioInputStream.getFormat().getFrameSize())];

            try {
                audioInputStream.read(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //convert sample bytes to channel separated 16 bit samples
            samplesContainer = getSampleArray(bytes);

            //find biggest sample. used for interpolating the yScaleFactor
            if (sampleMax > sampleMin) {
                biggestSample = sampleMax;
            } else {
                biggestSample = Math.abs(((double) sampleMin));
            }
        } catch (Exception e) {
        	Logger.getLogger(AudioInfo.class).debug(e);
//            e.printStackTrace();
        }
    }

    protected int[][] getSampleArray(byte[] eightBitByteArray) {
        int[][] toReturn = new int[getNumberOfChannels()][eightBitByteArray.length / (2 * getNumberOfChannels())];
        int index = 0;

        //loop through the byte[]
        for (int t = 0; t < eightBitByteArray.length;) {
            //for each iteration, loop through the channels
            for (int a = 0; a < getNumberOfChannels(); a++) {
	            	try {
	                //do the byte to sample conversion
	                //see AmplitudeEditor for more info
	                int low = (int) eightBitByteArray[t];
	                t++;
	                int high = (int) eightBitByteArray[t];
	                t++;
	                int sample = (high << 8) + (low & 0x00ff);
	
	                if (sample < sampleMin) {
	                    sampleMin = sample;
	                } else if (sample > sampleMax) {
	                    sampleMax = sample;
	                }
	                //set the value.
                	toReturn[a][index] = sample;
                } catch (ArrayIndexOutOfBoundsException e) {
                	Logger.getLogger(AudioInfo.class).trace(e);
                	//@author Stuart Thiel
                	//This seems to happen at times, don't know why. 
                	//The algorithm for this is unknown to me.
                	break;
                }
            }
            index++;
        }

        return toReturn;
    }

    public double getXScaleFactor(int panelWidth){
        return (panelWidth / ((double) samplesContainer[0].length));
    }

    public double getYScaleFactor(int panelHeight){
        return (panelHeight / (biggestSample * 2 * 1.2));
    }

    public int[] getAudio(int channel){
        return samplesContainer[channel];
    }

    public int getIncrement(double xScale) {
        try {
            int increment = (int) (samplesContainer[0].length / (samplesContainer[0].length * xScale));
            return increment;
        } catch (Exception e) {
        	Logger.getLogger(AudioInfo.class).error(e);
//            e.printStackTrace();
        }
        return -1;
    }

}

/*   Swing Hacks
 *   Tips and Tools for Killer GUIs
 * By Joshua Marinacci, Chris Adamson
 *   First Edition June 2005
 *   Series: Hacks
 *   ISBN: 0-596-00907-0
 *   http://www.oreilly.com/catalog/swinghks/
 */
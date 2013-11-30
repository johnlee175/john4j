package com.johnsoft.library.swing.effect;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A paint class that creates conical gradients around a given center point
 * It could be used in the same way as LinearGradientPaint and RadialGradientPaint
 * and follows the same syntax.
 * You could use floats from 0.0 to 1.0 for the fractions which is standard but it's
 * also possible to use angles from 0.0 to 360 degrees which is most of the times
 * much easier to handle.
 * Gradients always start at the top with a clockwise direction and you could
 * rotate the gradient around the center by given offset.
 * The offset could also be defined from -0.5 to +0.5 or -180 to +180 degrees.
 * If you would like to use degrees instead of values from 0 to 1 you have to use
 * the full constructor and set the USE_DEGREES variable to true
 * @version 1.0
 * @author hansolo
 */
public final class ConicalGradientPaint implements Paint
{
    private final Point2D m_center;
    private final double[] fraction_angles;
    private final double[] red_step_lookup;
    private final double[] green_step_lookup;
    private final double[] blue_step_lookup;
    private final double[] alpha_step_lookup;
    private final Color[] m_colors;
    private static final float INT_TO_FLOAT_CONST = 1f / 255f;

    /**
     * Standard constructor which takes the fractions in values from 0.0f to 1.0f
     * @param center
     * @param fractions
     * @param colors
     * @throws IllegalArgumentException
     */
    public ConicalGradientPaint(final Point2D center, final float[] fractions, final Color[] colors) throws IllegalArgumentException
    {
        this(false, center, 0.0f, fractions, colors);
    }

    /**
     * Enhanced constructor which takes the fractions in degress from 0.0f to 360.0f and
     * also an offset in degrees around the rotation center
     * @param use_degrees
     * @param center
     * @param offset
     * @param fractions
     * @param colors
     * @throws IllegalArgumentException
     */
    public ConicalGradientPaint(final boolean use_degrees, final Point2D center, final float offset, final float[] fractions, final Color[] colors) throws IllegalArgumentException
    {
        // Check that fractions and colors are of the same size
        if (fractions.length != colors.length)
        {
            throw new IllegalArgumentException("Fractions and colors must be equal in size");
        }

        final ArrayList<Float> fraction_list = new ArrayList<Float>(fractions.length);
        final float _offset;
        if (use_degrees)
        {
            final double deg_fraction = 1f / 360f;
            if (Double.compare((offset * deg_fraction), -0.5) == 0)
            {
                _offset = -0.5f;
            }
            else if (Double.compare((offset * deg_fraction), 0.5) == 0)
            {
                _offset = 0.5f;
            }
            else
            {
                _offset = (float) (offset * deg_fraction);
            }
            for (float fraction : fractions)
            {
                fraction_list.add((float) (fraction * deg_fraction));
            }
        }
        else
        {
            _offset = offset;
            for (float fraction : fractions)
            {
                fraction_list.add(fraction);
            }
        }
        
        // Check for valid offset
        if (_offset > 0.5f || _offset < -0.5f)
        {
            throw new IllegalArgumentException("Offset has to be in the range of -0.5 to 0.5");
        }

        // Adjust fractions and colors array in the case where startvalue != 0.0f and/or endvalue != 1.0f
        final List<Color> color_list = new ArrayList<Color>(colors.length);
        color_list.addAll(Arrays.asList(colors));

        // Assure that fractions start with 0.0f
        if (fraction_list.get(0) != 0.0f)
        {
            fraction_list.add(0, 0.0f);
            final Color tmp_color = color_list.get(0);
            color_list.add(0, tmp_color);
        }

        // Assure that fractions end with 1.0f
        if (fraction_list.get(fraction_list.size() - 1) != 1.0f)
        {
            fraction_list.add(1.0f);
            color_list.add(colors[0]);
        }

        // Recalculate the fractions and colors with the given offset
        final Map<Float, Color> fraction_colors = recalculate(fraction_list, color_list, _offset);

        // Clear the original FRACTION_LIST and COLOR_LIST
        fraction_list.clear();
        color_list.clear();

        // Sort the hashmap by fraction and add the values to the FRACION_LIST and COLOR_LIST
        final SortedSet<Float> sorted_fractions= new TreeSet<Float>(fraction_colors.keySet());
        final Iterator<Float> iterator = sorted_fractions.iterator();
        while (iterator.hasNext())
        {
            final float current_fraction = iterator.next();
            fraction_list.add(current_fraction);
            color_list.add(fraction_colors.get(current_fraction));
        }

        // Set the values
        this.m_center = center;
        m_colors = color_list.toArray(new Color[]{});

        // Prepare lookup table for the angles of each fraction
        final int max_fractions = fraction_list.size();
        this.fraction_angles = new double[max_fractions];
        for (int i = 0 ; i < max_fractions ; i++)
        {
            fraction_angles[i] = fraction_list.get(i) * 360;
        }

        // Prepare lookup tables for the color stepsize of each color
        red_step_lookup = new double[m_colors.length];
        green_step_lookup = new double[m_colors.length];
        blue_step_lookup = new double[m_colors.length];
        alpha_step_lookup = new double[m_colors.length];

        for (int i = 0 ; i < (m_colors.length - 1) ; i++)
        {
            red_step_lookup[i] = ((m_colors[i + 1].getRed() - m_colors[i].getRed()) * INT_TO_FLOAT_CONST) / (fraction_angles[i + 1] - fraction_angles[i]);
            green_step_lookup[i] = ((m_colors[i + 1].getGreen() - m_colors[i].getGreen()) * INT_TO_FLOAT_CONST) / (fraction_angles[i + 1] - fraction_angles[i]);
            blue_step_lookup[i] = ((m_colors[i + 1].getBlue() - m_colors[i].getBlue()) * INT_TO_FLOAT_CONST) / (fraction_angles[i + 1] - fraction_angles[i]);
            alpha_step_lookup[i] = ((m_colors[i + 1].getAlpha() - m_colors[i].getAlpha()) * INT_TO_FLOAT_CONST) / (fraction_angles[i + 1] - fraction_angles[i]);
        }
    }

    /**
     * Recalculates the fractions in the FRACTION_LIST and their associated colors in the COLOR_LIST with a given OFFSET.
     * Because the conical gradients always starts with 0 at the top and clockwise direction
     * you could rotate the defined conical gradient from -180 to 180 degrees which equals values from -0.5 to +0.5
     * @param fraction_list
     * @param color_list
     * @param offset
     * @return Hashmap that contains the recalculated fractions and colors after a given rotation
     */
    private HashMap<Float, Color> recalculate(final List<Float> fraction_list, final List<Color> color_list, final float offset)
    {
        // Recalculate the fractions and colors with the given offset
        final int max_fractions = fraction_list.size();
        final HashMap<Float, Color> fraction_colors = new HashMap<Float, Color>(max_fractions);
        for (int i = 0 ; i < max_fractions ; i++)
        {
            // Add offset to fraction
            final float tmp_fraction = fraction_list.get(i) + offset;

            // Color related to current fraction
            final Color tmp_color = color_list.get(i);

            // Check each fraction for limits (0...1)
            if (tmp_fraction <= 0)
            {
                fraction_colors.put(1.0f + tmp_fraction + 0.0001f, tmp_color);

                final float next_fraction;
                final Color next_color;
                if (i < max_fractions - 1)
                {
                    next_fraction = fraction_list.get(i + 1) + offset;
                    next_color = color_list.get(i + 1);
                }
                else
                {
                    next_fraction = 1 - fraction_list.get(0) + offset;
                    next_color = color_list.get(0);
                }
                if (next_fraction > 0)
                {
                    final Color new_fraction_color = getColorFromFraction(tmp_color, next_color, (int) ((next_fraction - tmp_fraction) * 10000), (int) ((-tmp_fraction) * 10000));
                    fraction_colors.put(0.0f, new_fraction_color);
                    fraction_colors.put(1.0f, new_fraction_color);
                }
            }
            else if(tmp_fraction >= 1)
            {
                fraction_colors.put(tmp_fraction - 1.0f - 0.0001f, tmp_color);

                final float previous_fraction;
                final Color previous_color;
                if (i > 0)
                {
                    previous_fraction = fraction_list.get(i - 1) + offset;
                    previous_color = color_list.get(i - 1);
                }
                else
                {
                    previous_fraction = fraction_list.get(max_fractions - 1) + offset;
                    previous_color = color_list.get(max_fractions - 1);
                }
                if (previous_fraction < 1)
                {
                    final Color new_fraction_color = getColorFromFraction(tmp_color, previous_color, (int) ((tmp_fraction - previous_fraction) * 10000), (int) (tmp_fraction - 1.0f) * 10000);
                    fraction_colors.put(1.0f, new_fraction_color);
                    fraction_colors.put(0.0f, new_fraction_color);
                }
            }
            else
            {
                fraction_colors.put(tmp_fraction, tmp_color);
            }
        }

        // Clear the original FRACTION_LIST and COLOR_LIST
        fraction_list.clear();
        color_list.clear();

        return fraction_colors;
    }

    /**
     * With the START_COLOR at the beginning and the DESTINATION_COLOR at the end of the given RANGE the method will calculate
     * and return the color that equals the given VALUE.
     * e.g. a START_COLOR of BLACK (R:0, G:0, B:0, A:255) and a DESTINATION_COLOR of WHITE(R:255, G:255, B:255, A:255)
     * with a given RANGE of 100 and a given VALUE of 50 will return the color that is exactly in the middle of the
     * gradient between black and white which is gray(R:128, G:128, B:128, A:255)
     * So this method is really useful to calculate colors in gradients between two given colors.
     * @param start_color
     * @param destination_color
     * @param range
     * @param value
     * @return Color calculated from a range of values by given value
     */
    public Color getColorFromFraction(final Color start_color, final Color destination_color, final int range, final int value)
    {
        final float source_red = start_color.getRed() * INT_TO_FLOAT_CONST;
        final float source_green = start_color.getGreen() * INT_TO_FLOAT_CONST;
        final float source_blue = start_color.getBlue() * INT_TO_FLOAT_CONST;
        final float source_alpha = start_color.getAlpha() * INT_TO_FLOAT_CONST;

        final float destination_red = destination_color.getRed() * INT_TO_FLOAT_CONST;
        final float destination_green = destination_color.getGreen() * INT_TO_FLOAT_CONST;
        final float destination_blue = destination_color.getBlue() * INT_TO_FLOAT_CONST;
        final float destination_alpha = destination_color.getAlpha() * INT_TO_FLOAT_CONST;

        final float red_delta = destination_red - source_red;
        final float green_delta = destination_green - source_green;
        final float blue_delta = destination_blue - source_blue;
        final float alpha_delta = destination_alpha - source_alpha;

        final float red_fraction = red_delta / range;
        final float green_fraction = green_delta / range;
        final float blue_fraction = blue_delta / range;
        final float alpha_fraction = alpha_delta / range;

        return new Color(source_red + red_fraction * value, source_green + green_fraction * value, source_blue + blue_fraction * value, source_alpha + alpha_fraction * value);
    }

    @Override
    public PaintContext createContext(final ColorModel color_model, final Rectangle device_bounds, final Rectangle2D user_bounds, final AffineTransform transform, final RenderingHints hints)
    {
        final Point2D transformed_center = transform.transform(m_center, null);
        return new ConicalGradientPaintContext(transformed_center);
    }

    @Override
    public int getTransparency()
    {
        return Transparency.TRANSLUCENT;
    }

    private final class ConicalGradientPaintContext implements PaintContext
    {
        final private Point2D center;

        public ConicalGradientPaintContext(final Point2D center)
        {
            this.center = new Point2D.Double(center.getX(), center.getY());
        }

        @Override
        public void dispose()
        {
        }

        @Override
        public ColorModel getColorModel()
        {
            return ColorModel.getRGBdefault();
        }

        @Override
        public Raster getRaster(final int x, final int y, final int tile_width, final int tile_height)
        {
            final double rotation_center_x = -x + center.getX();
            final double rotation_center_y = -y + center.getY();

            final int max = fraction_angles.length;

            // Create raster for given colormodel
            final WritableRaster raster = getColorModel().createCompatibleWritableRaster(tile_width, tile_height);

            // Create data array with place for red, green, blue and alpha values
            int[] data = new int[(tile_width * tile_height * 4)];

            double dx;
            double dy;
            double distance;
            double angle;
            double currentRed = 0;
            double currentGreen = 0;
            double currentBlue = 0 ;
            double currentAlpha = 0;

            for (int py = 0; py < tile_height; py++)
            {
                for (int px = 0; px < tile_width; px++)
                {

                    // Calculate the distance between the current position and the rotation angle
                    dx = px - rotation_center_x;
                    dy = py - rotation_center_y;
                    distance = Math.sqrt(dx * dx + dy * dy);

                    // Avoid division by zero
                    if (distance == 0)
                    {
                        distance = 1;
                    }

                    // 0 degree on top
                    angle = Math.abs(Math.toDegrees(Math.acos(dx / distance)));

                    if (dx >= 0 && dy <= 0)
                    {
                        angle = 90.0 - angle;
                    }
                    else if (dx >= 0 && dy >= 0)
                    {
                        angle += 90.0;
                    }
                    else if (dx <= 0 && dy >= 0)
                    {
                        angle += 90.0;
                    }
                    else if (dx <= 0 && dy <= 0)
                    {
                        angle = 450.0 - angle;
                    }

                    // Check for each angle in fractionAngles array
                    for (int i = 0 ; i < (max - 1) ; i++)
                    {
                        if ((angle >= fraction_angles[i]) )
                        {
                            currentRed = m_colors[i].getRed() * INT_TO_FLOAT_CONST + (angle - fraction_angles[i]) * red_step_lookup[i];
                            currentGreen = m_colors[i].getGreen() * INT_TO_FLOAT_CONST + (angle - fraction_angles[i]) * green_step_lookup[i];
                            currentBlue = m_colors[i].getBlue() * INT_TO_FLOAT_CONST + (angle - fraction_angles[i]) * blue_step_lookup[i];
                            currentAlpha = m_colors[i].getAlpha() * INT_TO_FLOAT_CONST + (angle - fraction_angles[i]) * alpha_step_lookup[i];
                            continue;
                        }
                    }

                    // Fill data array with calculated color values
                    final int base = (py * tile_width + px) * 4;
                    data[base + 0] = (int) (currentRed * 255);
                    data[base + 1] = (int) (currentGreen * 255);
                    data[base + 2] = (int) (currentBlue * 255);
                    data[base + 3] = (int) (currentAlpha * 255);
                }
            }

            // Fill the raster with the data
            raster.setPixels(0, 0, tile_width, tile_height, data);

            return raster;
        }
    }
}
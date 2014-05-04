package de.groth.dts.api.core.util.plugins;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.IDtsDto;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.plugins.IPlugin;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Base class for all plugin factories which create instances of concrete plugin
 * classes via reflection at runtime using {@link PluginConstructor}.<br/><br/>
 * 
 * The first generic parameter T defines the type of plugin that is used and the
 * second generic parameter defines the type of the concrete instance that will
 * be returned in {@link #instantiate(PluginInstantiationContext)}.
 * 
 * @param
 * <P>
 * the concrete {@link IPlugin}
 * @param <I>
 *                the concrete {@link IDtsDto}
 * 
 * @author Christian Groth
 * 
 */
public abstract class AbstractPluginFactory<P extends IPlugin, I extends IDtsDto> {
    private static final Logger LOGGER = Logger
            .getLogger(AbstractPluginFactory.class);

    /**
     * Instantiates a plugin of type T via reflection.
     * 
     * @param context
     *                the context
     * 
     * @return the instantiated plugin
     * 
     * @throws PluginInitializationException
     *                 the plugin initialization exception
     */
    @SuppressWarnings("unchecked")
    public I instantiate(final PluginInstantiationContext context)
            throws PluginInitializationException {
        AbstractPluginFactory.LOGGER.debug("instantiating plugin "
                + context.getPluginKey());
        final IDynamicTemplateSystemBase dts = context.getDtsBase();

        final P plugin = this.getPlugin(dts, context.getPluginKey());
        if (plugin == null || plugin.getPluginClassName() == null
                || plugin.getPluginClassName().trim().equals("")) {
            throw new PluginInitializationException("unknown plugin: " + plugin
                    + "!!");
        }
        AbstractPluginFactory.LOGGER.debug("plugin id " + plugin);

        try {
            // load the class
            AbstractPluginFactory.LOGGER.debug("loading the class "
                    + plugin.getPluginClassName());
            final Class<?> pluginDtoClass = Class.forName(plugin
                    .getPluginClassName());

            // define constructor parameter types and instances/values
            AbstractPluginFactory.LOGGER
                    .debug("creating arguments for plugin constructor call");
            final Class<?>[] arguments = new Class[1];
            arguments[0] = PluginInstantiationContext.class;
            final Object[] argumentList = new Object[1];
            argumentList[0] = context;

            // get constructor
            AbstractPluginFactory.LOGGER
                    .debug("searching for annotated constructor method in plugin class");
            Constructor<?> constructor = null;
            for (final Constructor<?> c : pluginDtoClass.getConstructors()) {
                if (c.getAnnotation(PluginConstructor.class) != null) {
                    AbstractPluginFactory.LOGGER
                            .debug("annotated constructor found");
                    constructor = c;
                    break;
                }
            }

            if (constructor == null) {
                throw new NoSuchMethodException(
                        "no annotated plugin constructor present in "
                                + pluginDtoClass.getName() + "!!");
            }

            // invoke constructor and return instance
            AbstractPluginFactory.LOGGER
                    .debug("invoking annotated constructor");
            return (I) constructor.newInstance(argumentList);
        } catch (final ClassNotFoundException ex) {
            throw new PluginInitializationException(
                    "unable to resolve parameter dto class ("
                            + plugin.getPluginClassName() + ")!!", ex);
        } catch (final SecurityException ex) {
            throw new PluginInitializationException(
                    "unable to invoke dto class constructor ("
                            + plugin.getPluginClassName() + ")!!", ex);
        } catch (final NoSuchMethodException ex) {
            throw new PluginInitializationException(
                    "unable to find dto class constructor ("
                            + plugin.getPluginClassName() + ")!!", ex);
        } catch (final IllegalArgumentException ex) {
            throw new PluginInitializationException(
                    "unable to set dto class constructor arguments ("
                            + plugin.getPluginClassName() + ")!!", ex);
        } catch (final InstantiationException ex) {
            throw new PluginInitializationException(
                    "unable to initialize dto class ("
                            + plugin.getPluginClassName() + ")!!", ex);
        } catch (final IllegalAccessException ex) {
            throw new PluginInitializationException(
                    "unable to acces dto class (" + plugin.getPluginClassName()
                            + ")!!", ex);
        } catch (final InvocationTargetException ex) {
            throw new PluginInitializationException(
                    "unable to invoke dto class ("
                            + plugin.getPluginClassName() + ")!!", ex);
        }
    }

    /**
     * Converts a plugin to its xml representation
     * 
     * @param instance
     *                the plugin to be converted
     * @param pluginNode
     *                parent node
     * @throws PluginInitializationException
     */
    public void toXml(final I instance, final Element pluginNode)
            throws PluginInitializationException {
        AbstractPluginFactory.LOGGER.debug("writing plugin to xml " + instance);

        try {
            // load the class
            AbstractPluginFactory.LOGGER.debug("loading the class "
                    + instance.getClass().getName());
            final Class<?> pluginDtoClass = Class.forName(instance.getClass()
                    .getName());

            // define method parameter types and instances/values
            AbstractPluginFactory.LOGGER
                    .debug("creating arguments for plugin method call");
            final Class<?>[] arguments = new Class[1];
            arguments[0] = PluginInstantiationContext.class;
            final Object[] argumentList = new Object[1];
            argumentList[0] = pluginNode;

            // get method
            AbstractPluginFactory.LOGGER
                    .debug("searching for annotated toXml-method in plugin class");
            Method method = null;
            for (final Method m : pluginDtoClass.getMethods()) {
                if (m.getAnnotation(PluginToXml.class) != null) {
                    AbstractPluginFactory.LOGGER
                            .debug("annotated toXml-method found");
                    method = m;
                    break;
                }
            }

            if (method == null) {
                throw new NoSuchMethodException(
                        "no annotated plugin toXml-method present in "
                                + pluginDtoClass.getName() + "!!");
            }

            AbstractPluginFactory.LOGGER
                    .debug("invoking annotated toXml-method");
            method.invoke(instance, argumentList);
        } catch (final ClassNotFoundException ex) {
            throw new PluginInitializationException(
                    "unable to resolve parameter dto class ("
                            + instance.getClass().getName() + ")!!", ex);
        } catch (final SecurityException ex) {
            throw new PluginInitializationException("unable to invoke method ("
                    + instance.getClass().getName() + ")!!", ex);
        } catch (final NoSuchMethodException ex) {
            throw new PluginInitializationException("unable to find method ("
                    + instance.getClass().getName() + ")!!", ex);
        } catch (final IllegalArgumentException ex) {
            throw new PluginInitializationException(
                    "unable to set method arguments ("
                            + instance.getClass().getName() + ")!!", ex);
        } catch (final IllegalAccessException ex) {
            throw new PluginInitializationException(
                    "unable to acces dto class ("
                            + instance.getClass().getName() + ")!!", ex);
        } catch (final InvocationTargetException ex) {
            throw new PluginInitializationException(
                    "unable to invoke dto class ("
                            + instance.getClass().getName() + ")!!", ex);
        } // catch
    }

    /**
     * To be implemented by concrete subclasses to return the concrete plugin
     * 
     * @param dts
     * @param pluginKey
     * @return concrete Plugin
     */
    protected abstract P getPlugin(IDynamicTemplateSystemBase dts,
            String pluginKey);
}

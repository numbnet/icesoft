package com.icesoft.faces.webapp.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class BoundedQueueManager implements Queue.Manager {
    private Map queues = new HashMap();
    private ArrayList listeners = new ArrayList();

    public Queue create(String view) {
        Queue queue = new BoundedQueue(view);
        queues.put(view, queue);
        return queue;
    }

    public void listenWith(Queue.Listener listener) {
        listeners.add(listener);
    }

    public void broadcast(Command command) {
        Iterator i = queues.values().iterator();
        while (i.hasNext()) {
            Queue queue = (Queue) i.next();
            queue.queue(command);
        }
    }

    public void emptyQueues() {
        Iterator i = queues.values().iterator();
        while (i.hasNext()) {
            Queue queue = (Queue) i.next();
            queue.dequeueAll();
        }
    }

    public Command[] collectFrom(String[] views) {
        ArrayList result = new ArrayList();
        for (int i = 0; i < views.length; i++) {
            Queue queue = (Queue) queues.get(views[i]);
            while (queue.isNotEmpty()) {
                result.add(queue.dequeue());
            }
        }

        return (Command[]) result.toArray(new Command[result.size()]);
    }

    private void broadcastQueued(String view, Queue queue) {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            Queue.Listener listener = (Queue.Listener) i.next();
            listener.queued(view, queue);
        }
    }

    private void broadcastDequeued(String view, Queue queue) {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            Queue.Listener listener = (Queue.Listener) i.next();
            listener.dequeued(view, queue);
        }
    }

    private class BoundedQueue implements Queue {
        private static final int LIMIT = 50;
        private LinkedList commands = new LinkedList();
//        private Command command;
        private String view;

        public BoundedQueue(String view) {
            this.view = view;
        }

        public boolean isNotEmpty() {
            return !commands.isEmpty();
//            return command != null;
        }

        public boolean isEmpty() {
            return commands.isEmpty();
//            return command == null;
        }

        public void queue(Command newCommand) {
            if (commands.size() > LIMIT) {
                throw new IllegalStateException("Queue has reached the upper limit");
            }
            commands.addLast(newCommand);
//            command = command == null ? newCommand : newCommand.coallesceWith(command);
            broadcastQueued(view, this);
        }

        public Command dequeue() {
            Command dequeuedCommand = (Command) commands.removeFirst();
//            Command dequeuedCommand = command;
//            command = null;
            broadcastDequeued(view, this);
            return dequeuedCommand;
        }

        public Command[] dequeueAll() {
            Command[] cs = (Command[]) this.commands.toArray(new Command[this.commands.size()]);
//            Command[] cs = new Command[] { dequeue() };
            broadcastDequeued(view, this);
            return cs;
        }
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class WordCount {

    public static void main(String[] args) throws Exception {

        Job job = Job.getInstance(new Configuration(), "wordcount");
        for (int i = 0; i < args.length; i++) {
            if ("-skip".equals(args[i])) {
                job.getConfiguration().setBoolean("skip", true);
                job.addCacheFile(new Path(args[i + 1]).toUri());
            }
        }
        job.setJarByClass(WordCount.class);
        job.setMapperClass(MyMapper.class);
        job.setCombinerClass(MyReducer.class);
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private Set<String> skipWords = new HashSet<String>();

        protected void setup(Context context) throws IOException {
            Configuration config = context.getConfiguration();
            if (config.getBoolean("skip", false)) {
                URI[] localPaths = context.getCacheFiles();
                readSkipWordFile(localPaths[0]);
            }
        }

        private void readSkipWordFile(URI patternsURI) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(patternsURI.getPath()).getName()));
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    skipWords.add(currentLine.toLowerCase().trim());
                }
            } catch (IOException ioe) {
                //System.out.println(ioe.printStackTrace());
                System.out.println(ioe);
            }
        }

        @Override
        public void map(LongWritable offset, Text lineText, Context context) throws IOException, InterruptedException {
            String line = lineText.toString().toLowerCase();
            Text text;
            for (String words : line.split("\\s*\\b\\s*")) {
                if (words.isEmpty() || skipWords.contains(words.toLowerCase().trim())) {
                    continue;
                }
                text = new Text(words);
                context.write(text, new IntWritable(1));
            }
        }
    }

    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text word, Iterable<IntWritable> counts, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable count : counts) {
                sum += count.get();
            }
            context.write(word, new IntWritable(sum));
        }
    }
}
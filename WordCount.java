/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Random;
import java.lang.Runtime;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCount {

  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());


        Random generator = new Random();
        int r = generator.nextInt();
        int n = 1;
        //if (r % 2 == 0)
        //    n = r;
        context.write(word, new IntWritable(n));

      }
    }
  }
  
  public static class IntSumReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
       //System.out.println("****************Reduce" + key + " : " + values.getClass());
       Random generator = new Random();
       int r = generator.nextInt();
       int sum = 0;
      if (r % 2 == 0)
          sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }
    boolean success = false;
   // while (!success)
    {
        //Job job = new Job(conf, "word count");
        //FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        //FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        Job.run(conf, "word count", WordCount.class, TokenizerMapper.class, IntSumReducer.class,IntSumReducer.class, Text.class, IntWritable.class, new Path(otherArgs[0]), new Path(otherArgs[1]));
        /*
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        success = job.waitForCompletion(true);
        System.out.println(success);
*/
        if (!success)
        {
            //FileUtils.deleteDirectory(new File("/Users/cong/MyClass/DependableDistributedSystems/project/wordcount/output"));
        //    Runtime.getRuntime().exec("rm -r /Users/cong/MyClass/DependableDistributedSystems/project/wordcount/output");
        }            
    }
    System.exit(success ? 0 : 1);
  }
}

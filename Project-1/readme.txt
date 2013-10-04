Search Program that uses Lucene search lib
How to run:
1. From command prompt, change dir to ../Project1
2. java -cp "bin;lib/*" lucene.Controller

Using the program:
1. Type in the query in text field
2. Pressing enter will start the search
3. If re-index is checked, search will take a while because re-indexing will take place first (normally about 2 seconds, can see progress in the console)
4. To refine query manually, select relevant results (hold Ctrl to select multiple items). Click on the dropdown on the top right ('Search') and select 'Refine Search', then press Enter
5. Import query runs batch search and export the result as .txt in the same folder as the input file.
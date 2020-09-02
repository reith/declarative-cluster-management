# Requirements

- docker

# Artifact goals

We will reproduce the key results from the Kubernetes scheduler evaluation in section 6.1. 
Given that some of the experiments were run on a 500 node AWS cluster in the paper, we 
will use the simulation set up described in the "effect of increased cluster sizes" 
experiment to run DCM on a single host. We will reproduce Table 1, and figures 13, 
14 and 15, but for DCM and with sufficient fidelity.


# Instructions to produce results

1. Run the following command to launch a docker image with all the dependencies installed. The image is
   roughly 800MB in size:

 `$: docker run -it lalithsuresh/dcm-osdi-artifact bash`


2. Once in the docker image, run the following command to clone the DCM respository's artifact branch:

 ```
  $: git clone http://github.com/vmware/declarative-cluster-management --single-branch --branch artifact
 ```

3. Run the experiment (takes roughly 30-40 minutes):

 ```
  $: cd declarative-cluster-management
  $: ./run_emulation.sh 20000
 ```
 
 Note: the results for the paper were produced with longer runs using `./run_emulation.sh 200000`, which takes roughly 
 3-4 hours to execute. You'll find the shorter run produces similar plots.

4. Once the command completes, confirm that it has created a `plots/` folder with the following files:
   * schedulingLatencyEcdfVaryFN=500PlotLocal.pdf (Figure 13, DCM)
   * dcmLatencyBreakdownVaryFN=500PlotLocal.pdf   (Figure 14)
   * dcmLatencyBreakdownF=100VaryNPlotLocal.pdf   (Figure 15)
   * modelSizeTable.tex                           (Table 1)

5. To view the above files, open another terminal and copy out the results from the docker image (note: the
   below command runs on your host, not inside the docker image. It assumes there is only one instance
   of the docker image running):

 ```
  $: ARTIFACT_IMAGE=`docker ps | grep "dcm-osdi-artifact" | awk '{print $1}'`
  $: docker cp $ARTIFACT_IMAGE:/declarative-cluster-management/plots .
 ```


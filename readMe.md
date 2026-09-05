# Tester App
Create and solve tests to study for your exams.

## What does this thing look like?
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/b67bbb0e-a169-434a-b499-967760d42262" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/6835c5ae-ba76-470d-a928-6b97b137776e" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/55da01b1-4077-47af-9472-c64fc126edb8" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/5e8f60a3-7bd2-4ef3-8867-f706fd87b137" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/1ca05bf1-8379-4d61-b808-c515de42e079" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/7e716b09-93d7-470f-80fd-456ee7d677f7" />

## Download
[Releases](https://github.com/Selenorius/Tester-App/releases)

## Guide
### Step 1: Create a topic
1. Click on the big *PLUS* button
2. Choose *Create new topic*

### Step 2: Create an exam
1. Click on the *Add exam* button
2. Click on the big *PLUS* button
3. Choose an option

### Step 3: Solve your exam
1. Start **Tester App**.
2. Pick your **Topic**.
3. Pick your **Exam**.

Now you can solve your **Exam**!

## Help
### Image Formats
- .png
- .jpg

### Jar Location
The **Jar** file is in *tester_app/app/build/libs*, but you can also find it in [releases](https://github.com/Selenorius/Tester-App/releases).

### Create an exam by hand
1. Open/create a directory and create a new textfile with the name of your exam.
2. Now open the textfile created in step 1 and add your questions in the following format:

```r
{
    {
        TRUE_FALSE {
            "True/False Question text"
            {
                TRUE
            }
        }
    }
    {
        MULTIPLE_CHOICE ORDERED {
            "Multiple Choice Question text"
            {
                TRUE {
                    "Option 1 text"
                    "Option 1 alternative text"
                }
                FALSE {
                    "Option 2 text"
                }
                FALSE {
                    "Option 3 text"
                }
            }
        }
    }
    {
        WRITTEN ORDERED {
            "Written Question text"
            {
                TRUE {
                    "Option 1 text"
                    "Option 1 alternative text"
                }
                FALSE {
                    "Option 2 text"
                }
                FALSE {
                    "Option 3 text"
                }
            }
        }
    }
}
```

| Enum | Description |
|:--------------- |:-------------------------- |
| MULTIPLE_CHOICE or MC | Multiple Choice Question, can be ORDERED |
| TRUE_FALSE or TF | True/False Question |
| - | Written Question, can be ORDERED |

If **Type** is left empty it will be set to *WRITTEN*.

A **TextOption** has to contain an **Array** of any amount of **String** *text*.

A **ButtonOption** has to contain a **String** *text* and a **Boolean** of value *true* or *false*.

True/False Questions have to contain a **Boolean** of value *true* or *false*.

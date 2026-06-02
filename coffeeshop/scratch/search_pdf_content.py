import sys
import PyPDF2

sys.stdout.reconfigure(encoding='utf-8')

pdf_path = 'd:/Project/BTL-CNPM/BG CNPM 2020.pdf'
reader = PyPDF2.PdfReader(pdf_path)

# Let's search for JUnit or Blackbox pages
pages_to_read = [198, 199, 214, 215] # 0-indexed page indices around the outline page numbers. Wait, outline says page 198, but page numbers in PDF might have an offset. Let's find pages that have the word "Blackbox test" or "JUnit test"

for idx, page in enumerate(reader.pages):
    text = page.extract_text()
    if '9.5.2' in text or '9.5.3' in text or 'Blackbox test' in text:
        print(f"PDF Page index {idx} (Document Page {idx+1}):")
        lines = text.split('\n')
        for line in lines[:10]: # print first 10 lines of the matching page
            print(f"  {line}")
